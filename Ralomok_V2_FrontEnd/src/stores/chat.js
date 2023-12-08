import { ref, computed } from "vue";
import { open, close, send } from "../api/socket";
import { defineStore } from "pinia";

export const useChatStore = defineStore("chat", () => {
  const userName = ref("");
  const texts = ref([]);
  const state = ref("disconnected");

  function connect() {
    open(
      // onOpen
      (object) => {
        console.log("연결성공", object);
        state.value = "connected";
        send({ name: "config", data: userName.value });
      },
      // onMessage
      ({ data }) => {
        console.log(JSON.parse(data));
        texts.value.push(JSON.parse(data));
      },
      // onClose
      () => {
        console.log("연결종료");
        state.value = "disconnected";
      },
      // onError
      (error) => {
        console.log(error);
      }
    );
  }

  function sendData(object) {
    send(object);
  }

  function disconnect() {
    close();
  }

  return { userName, texts, state, connect, sendData, disconnect };
});

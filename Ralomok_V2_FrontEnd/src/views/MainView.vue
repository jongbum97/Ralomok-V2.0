<script setup>
import { useChatStore } from "@/stores/chat";
import { storeToRefs } from "pinia";

const store = useChatStore();
const { userName } = storeToRefs(store);
</script>

<template>
  <main class="container p-5 m-5">
    <div class="container border">
      <div class="m-5">
        <h1 class="text-center">메인 페이지</h1>
      </div>
      <div class="p-5 text-center">
        <input
          class="m-2"
          type="text"
          placeholder="이름"
          v-model="userName"
          @keydown.enter="$router.push({ name: 'chat' })"
        />
        <input
          class="m-2"
          type="button"
          value="채팅방 입장"
          @click="$router.push({ name: 'chat' })"
        />
      </div>
    </div>
  </main>
</template>

<style scoped></style>

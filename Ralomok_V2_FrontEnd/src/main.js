import "@/assets/css/main.css";

import {createApp} from "vue";
import App from "./App.vue";
import router from "./router";
import {createPinia} from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";


import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap'

const pinia = createPinia();
const app = createApp(App);

pinia.use(piniaPluginPersistedstate);
app.use(router);
app.use(pinia);

app.mount("#app");
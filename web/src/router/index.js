import { createRouter, createWebHistory } from "vue-router";
import PkIndexView from "../views/pk/PkIndexView";
import RecordIndexView from "../views/record/RecordIndexView";
import RankListIndexView from "../views/ranklist/RankListIndexView";
import UserBotIndexView from "../views/user/UserBotIndexView";
import NotFound from "../views/error/NotFound";
const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/",
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkIndexView,
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RankListIndexView,
  },
  {
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserBotIndexView,
  },
  {
    path: "/404/",
    name: "404NotFound_index",
    component: NotFound,
  },
  {
    path: "/:catchAll(.*)",
    name: "404NotFound",
    redirect: "/404/",
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

<template>
  <ContentField>我的bot</ContentField>
</template>

<script>
import ContentField from "../../../components/ContentField";
import $ from "jquery";
import { useStore } from "vuex";
export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/bot/add/",
    //   type: "post",
    //   data: {
    //     title: "Bot的标题",
    //     description: "Bot的描述",
    //     content: "Bot的内容",
    //   },
    //   headers: {
    //     Authorization: "Bearer " + store.state.user.token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   },
    // });
    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/bot/remove/",
    //   type: "post",
    //   data: {
    //     bot_id: 16,
    //   },
    //   headers: {
    //     Authorization: "Bearer " + store.state.user.token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   },
    // });
    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/bot/update/",
    //   type: "post",
    //   data: {
    //     bot_id: 14,
    //     title: "新title",
    //     description: "新描述",
    //     content: "新代码",
    //   },
    //   headers: {
    //     Authorization: "Bearer " + store.state.user.token,
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   },
    // });
    $.ajax({
      url: "http://127.0.0.1:3000/user/bot/getlist/",
      type: "get",
      headers: {
        Authorization: "Bearer " + store.state.user.token,
      },
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      },
    });
  },
};
</script>

<style scoped></style>

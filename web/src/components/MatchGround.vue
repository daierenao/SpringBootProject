<template>
  <div class="matchground">
    <div class="row">
      <div class="col-4">
        <div class="user-photo">
          <img :src="$store.state.user.photo" alt="" />
        </div>
        <div class="user-username">
          {{ $store.state.user.username }}
        </div>
      </div>
      <div class="col-4">
        <div class="user-select-bot">
          <select
            v-model="select_bot_id"
            class="form-select"
            aria-label="Default select example"
          >
            <option value="-1" selected>亲自对战</option>
            <option v-for="bot in bots" :key="bot.id" :value="bot.id">
              {{ bot.title }}
            </option>
          </select>
        </div>
      </div>
      <div class="col-4">
        <div class="user-photo">
          <img :src="$store.state.pk.opponent_photo" alt="" />
        </div>
        <div class="user-username">
          {{ $store.state.pk.opponent_username }}
        </div>
      </div>
      <div class="col-12" style="text-align: center; padding-top: 15vh">
        <button @click="click_match_btn" type="button" class="btn btn-warning">
          {{ match_btn }}
        </button>
      </div>
    </div>
  </div>
</template>
<script>
import { ref } from "vue";
import { useStore } from "vuex";
import $ from "jquery";
export default {
  setup() {
    let match_btn = ref("开始匹配");
    let bots = ref([]);
    let select_bot_id = ref("-1");
    const store = useStore();
    const refresh_bot = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/getlist/",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          bots.value = resp;
        },
        error(resp) {
          console.log(resp);
        },
      });
    };
    refresh_bot();
    const click_match_btn = () => {
      if (match_btn.value === "开始匹配") {
        match_btn.value = "取消";
        console.log(select_bot_id.value);
        store.state.pk.socket.send(
          JSON.stringify({
            event: "start-matching",
            bot_id: select_bot_id.value,
          })
        );
      } else {
        match_btn.value = "开始匹配";
        store.state.pk.socket.send(
          JSON.stringify({
            event: "stop-matching",
          })
        );
      }
    };

    return {
      match_btn,
      click_match_btn,
      bots,
      select_bot_id,
    };
  },
};
</script>

<style scoped>
div.matchground {
  width: 60vw;
  height: 70vh;
  background-color: rgba(50, 50, 50, 0.5);
  margin: 40px auto;
}
.user-photo {
  text-align: center;
  padding-top: 10vh;
}
.user-photo > img {
  border-radius: 50%;
  width: 20vh;
}
.user-username {
  text-align: center;
  /* padding-top: 5vh; */
  font-size: 20px;
  font-weight: 600;
  color: white;
  padding-top: 2vh;
}
.user-select-bot {
  padding-top: 20vh;
}
.user-select-bot > select {
  width: 60%;
  margin: 0 auto;
}
</style>

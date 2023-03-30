import $ from "jquery";

export default {
  state: {
    id: "",
    username: "",
    photo: "",
    rating: 0,
    token: "",
    is_login: false,
    pulling_info: false,
  },
  getters: {},
  mutations: {
    updateUser(state, user) {
      (state.id = user.id),
        (state.username = user.username),
        (state.photo = user.photo),
        (state.is_login = user.is_login);
      state.rating = user.rating;
    },
    updateToken(state, token) {
      state.token = token;
    },
    logout(state) {
      (state.id = ""),
        (state.username = ""),
        (state.photo = ""),
        (state.rating = 0),
        (state.token = ""),
        (state.is_login = false);
      state.pulling_info = false;
    },
    updatePullingInfo(state, pulling_info) {
      state.pulling_info = pulling_info;
    },
  },
  actions: {
    login(context, data) {
      $.ajax({
        url: "http://127.0.0.1:3000/user/account/token/",
        type: "post",
        data: {
          username: data.username,
          password: data.password,
        },
        success(resp) {
          if (resp.error_message === "success") {
            context.commit("updateToken", resp.token);
            localStorage.setItem("jwt_token", resp.token);
            data.success();
          } else {
            data.error(resp);
          }
        },
        error(resp) {
          data.error(resp);
        },
      });
    },
    getInfo(context, data) {
      $.ajax({
        url: "http://127.0.0.1:3000/user/account/info/",
        type: "get",
        headers: {
          Authorization: "Bearer " + context.state.token,
        },
        success(resp) {
          if (resp.error_message === "success") {
            context.commit("updateUser", {
              ...resp,
              is_login: true,
            });
            data.success(resp);
          } else {
            data.error(resp);
          }
        },
        error(resp) {
          data.error(resp);
        },
      });
    },
    logout(context) {
      context.commit("logout");
      localStorage.removeItem("jwt_token", context.state.token);
    },
  },
  modules: {},
};

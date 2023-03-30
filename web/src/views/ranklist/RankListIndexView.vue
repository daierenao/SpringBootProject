<template>
  <ContentField>
    <table class="table table-striped table-hover" style="text-align: center">
      <thead>
        <tr>
          <th>排名</th>
          <th>玩家</th>
          <th>天梯分</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(user, index) in ranklists" :key="user.id">
          <td>
            {{ index + (rank - 1) * 10 + 1 }}
          </td>
          <td>
            <img :src="user.photo" alt="" />
            &nbsp;
            {{ user.username }}
          </td>
          <td>{{ user.rating }}</td>
        </tr>
      </tbody>
    </table>
    <nav aria-label="Page navigation example" style="float: right">
      <ul class="pagination">
        <li @click="click_page_btn(-2)" class="page-item">
          <a class="page-link" href="####">前一页</a>
        </li>
        <li
          @click="click_page_btn(page.number)"
          :class="'page-item ' + page.is_active"
          v-for="page in pages"
          :key="page.number"
        >
          <a class="page-link" href="####">{{ page.number }}</a>
        </li>
        <li @click="click_page_btn(-1)" class="page-item">
          <a class="page-link" href="####">后一页</a>
        </li>
      </ul>
    </nav>
  </ContentField>
</template>

<script>
import ContentField from "../../components/ContentField";
import { ref } from "vue";
import $ from "jquery";
import { useStore } from "vuex";
export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    let ranklists = ref([]);
    let current_page = 1;
    let total_users = 0;
    let pages = ref([]);
    let rank = ref(0);
    const click_page_btn = (page) => {
      if (page === -2) {
        page = current_page - 1;
      } else if (page === -1) {
        page = current_page + 1;
      }
      let max_pages = parseInt(Math.ceil(total_users / 10));
      if (page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    };

    const update_page = () => {
      let max_pages = parseInt(Math.ceil(total_users / 10));
      let new_pages = [];
      for (let i = current_page - 2; i <= current_page + 2; i++) {
        if (i >= 1 && i <= max_pages) {
          new_pages.push({
            number: i,
            is_active: i === current_page ? "active" : "",
          });
        }
      }
      pages.value = new_pages;
    };

    const pull_page = (page) => {
      current_page = page;
      $.ajax({
        url: "http://127.0.0.1:3000/ranklist/getlist/",
        type: "get",
        data: {
          page,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          console.log(resp.users);
          ranklists.value = resp.users;
          total_users = resp.users_count;
          update_page();
          rank.value = current_page;
        },
        error(resp) {
          console.log(resp);
        },
      });
    };
    pull_page(current_page);
    return {
      ranklists,
      click_page_btn,
      pages,
      rank,
    };
  },
};
</script>
<style scoped>
img {
  border-radius: 50%;
  width: 6vh;
}
</style>

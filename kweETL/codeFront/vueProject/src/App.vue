<template>
<div class="indexMain">
  <router-view/>
</div>
    
</template>

<script>
import HeaderNav from "./components/header.vue";
export default {
  name: "App",
  components: {
    HeaderNav
  },
  methods: {
  }
};
</script>

<style>
*{
  margin:0;
  padding: 0;
}
.indexMain{
  height: 100%;
}
.el-header {
  background-color: #b3c0d1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}
</style>

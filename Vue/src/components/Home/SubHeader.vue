<template>
  <div style="height: 92px; line-height: 50px; border-bottom: 1px solid #ccc; display: flex; background-color: rgb(199,237,204)">
    <!--    欢迎语-->
    <div style="padding-top: 16px; padding-left: 20px"><img src="../../assets/avatar.png" height="60px" width="60px"></div>
    <div style="padding-top: 20px; padding-left: 300px; text-align: center; font-size: 30px; color: #606266">
      Welcome, {{this.user.name}} !!</div>
    <div style="flex: 1"/>
    <!--    三大模块-->
    <div style="padding-top: 18px; padding-right: 120px">
      <el-row>
        <el-button type="success" plain @click="gotoprojl">我的项目</el-button>
        <el-button type="success" plain @click="gototaskl" style="margin-left: 50px">我的任务</el-button>
        <el-button type="success" plain @click="gotogroupl" style="margin-left: 50px">我的团队</el-button>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: "SubHeader",
  data(){
    return{
      user: ''
    }
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("user"))
  },
  methods: {
    gotoprojl(){
      this.$router.push('/projlist')
    },
    gototaskl(){
      this.$router.push('/tasklist')
    },
    gotogroupl(){
      this.$router.push('/grouplist')
    }
  }
}
</script>

<style scoped>

</style>
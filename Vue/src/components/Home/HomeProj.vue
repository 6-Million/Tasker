<template>
  <div style="margin-right: 10px; height: 535px; width: 455px;background-color: white">
    <div style="height: 15px; margin-left: 15px; padding-top: 9px">
      我的项目
      <a class="link" style="margin-left: 280px" @click="gotolist">全部项目...</a>
    </div>
    <el-divider/>
    <el-card class="card" v-if="tableData.length>0" shadow="hover" @click.native="gotoproj(tableData[0].pid)" style="height: 120px; width: 395px; margin-left: 30px; background-color: rgb(199,237,204)">
      {{tableData[0].title}}
      <br/>
      <br/>
      截止时间：{{tableData[0].ddl}}
    </el-card>
    <el-card class="card" v-if="tableData.length>1" shadow="hover" @click.native="gotoproj(tableData[1].pid)" style="height: 120px; width: 395px; margin-top: 35px; margin-left: 30px; background-color: rgb(199,237,204)">
      {{tableData[1].title}}
      <br/>
      <br/>
      截止时间：{{tableData[1].ddl}}
    </el-card>
    <el-card class="card" v-if="tableData.length>2" shadow="hover" @click.native="gotoproj(tableData[2].pid)" style="height: 120px; width: 395px; margin-top: 35px; margin-left: 30px; background-color: rgb(199,237,204)">
      {{tableData[2].title}}
      <br/>
      <br/>
      截止时间：{{tableData[2].ddl}}
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "HomeProj",
  data(){
    return{
      tableData:[],
      user: ''
    }
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("user"))
    this.load()
  },
  methods: {
    gotoproj(val){
      this.$router.push({
        path:'/project',
        query:{
          projectId: val
        }
      })
    },
    gotolist(){
      this.$router.push('/projlist')
    },
    load(){
      request.get("/project/load", {params:{pageNum: 1, pageSize:3, uid: this.user.uid}}).then(res => {
        this.tableData = res.data.records;
      })
    },
  }
}
</script>

<style scoped>
    .link:hover{
      cursor: pointer;
      color: limegreen;
    }
    .card:hover{
      cursor: pointer;
    }
</style>
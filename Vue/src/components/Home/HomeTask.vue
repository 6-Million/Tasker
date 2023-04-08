<template>
  <div style="margin-left:10px; height: 350px; width: 1050px; background-color: white">
    <div style="height: 15px; margin-left: 25px; padding-top: 9px">
      我的任务
      <a class="link" @click="gotolist" style="margin-left: 860px">全部任务...</a>
    </div>
    <el-divider/>
    <el-row :gutter="60" style="margin-left: 20px">
      <el-col v-if="tableData.length>0" class="card" :span="7">
        <el-card shadow="hover" @click.native="gototask(tableData[0].tid)" style="height: 250px; background-color: rgb(199,237,204)">
          <br/>
          {{tableData[0].title}}
          <br/>
          <br/>
          截止时间：{{tableData[0].ddl}}
        </el-card>
      </el-col>
      <el-col v-if="tableData.length>1" class="card" :span="7">
        <el-card shadow="hover" @click.native="gototask(tableData[1].tid)" style="height: 250px; background-color: rgb(199,237,204)">
          <br/>
          {{tableData[1].title}}
          <br/>
          <br/>
          截止时间：{{tableData[1].ddl}}
        </el-card>
      </el-col>
      <el-col v-if="tableData.length>2" class="card" :span="7">
        <el-card shadow="hover" @click.native="gototask(tableData[2].tid)" style="height: 250px; background-color: rgb(199,237,204)">
          <br/>
          {{tableData[2].title}}
          <br/>
          <br/>
          截止时间：{{tableData[2].ddl}}
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "HomeTask",
  data(){
    return{
      user: "",
      tableData:[]
    }
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("user"))
    this.load()
  },
  methods: {
    gototask(val){
      this.$router.push({
        path:'/task',
        query:{
          taskId: val
        }
      })
    },
    gotolist(){
      this.$router.push('/tasklist')
    },
    load(){
      request.get("/task/load", {params:{pageNum: 1, pageSize:3, uid: this.user.uid}}).then(res => {
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
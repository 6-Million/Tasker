<template>
  <div style="margin-left:10px; height: 175px; width: 1050px; background-color: white">
    <div style="height: 15px; margin-left: 25px; padding-top: 9px">
      我的团队
      <a class="link" @click="gotolist" style="margin-left: 860px">全部团队...</a>
    </div>
    <el-divider/>
    <el-row :gutter="60" style="margin-left: 20px">
      <el-col class="card" v-if="tableData.length>0" :span="7">
        <el-card shadow="hover" @click.native="gotogroup(tableData[0].groupKey)" style="height: 90px; margin-top: -8px; background-color: rgb(199,237,204)">
          {{tableData[0].groupName}}
        </el-card>
      </el-col>
      <el-col class="card" v-if="tableData.length>1" :span="7">
        <el-card shadow="hover" @click.native="gotogroup(tableData[1].groupKey)" style="height: 90px; margin-top: -8px; background-color: rgb(199,237,204)">
          {{tableData[1].groupName}}
        </el-card>
      </el-col>
      <el-col class="card" v-if="tableData.length>2" :span="7">
        <el-card shadow="hover" @click.native="gotogroup(tableData[2].groupKey)" style="height: 90px; margin-top: -8px; background-color: rgb(199,237,204)">
          {{tableData[2].groupName}}
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "HomeTeam",
  data(){
    return{
      user: '',
      tableData: []
    }
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("user"))
    this.load()
  },
  methods: {
    gotogroup(val){
      this.$router.push({
        path:'/group',
        query:{
          groupKey: val
        }
      })
    },
    gotolist(){
      this.$router.push('/grouplist')
    },
    load(){
      request.get("/group/load", {params:{pageNum: 1, pageSize:3, uid: this.user.uid}}).then(res => {
        this.tableData = res.data.records;
      })
    }
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
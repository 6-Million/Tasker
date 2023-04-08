<template>
  <div>
    <Header/>
    <div style="margin: 10px 10px; height: 630px; background-color: white">
      <div style="padding-top: 10px; color: #606266; font-size: 25px; text-align: center">审批历史记录</div>
      <!--      表格展示-->
      <div style="padding: 20px">
        <el-table
            :data="tableData"
            border
            stripe
            style="width: 100%">
          <el-table-column
              prop="pass"
              label="审批操作"
              width="300">
          </el-table-column>
          <el-table-column
              prop="comment"
              label="留言">
          </el-table-column>
        </el-table>
        <!--        分页-->
        <el-pagination
            @current-change="handleCurrentChange"
            :current-page.sync="currentPage"
            layout="prev, pager, next"
            :total="100"
            style="margin-top: 20px; margin-left: 550px;">
        </el-pagination>
      </div>
    </div>
    <Footer/>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import request from "@/utils/request";

export default {
  name: "Comment",
  components: {Footer, Header},
  data(){
    return{
      user: "",
      taskId: '',
      tableData: [],
      currentPage: 1,
    }
  },
  created(){
    this.user = JSON.parse(sessionStorage.getItem("user"));
    this.check();
    this.getParams();
    this.load();
  },
  methods: {
    check(){
      if(!this.user)
        this.$router.push('/login')
    },
    getParams(){
      this.taskId = this.$route.query.taskId;
    },
    handleCurrentChange(val){
      this.currentPage = val;
      this.load()
    },
    load(){
      request.get("/comment/load",{params:{pageNum: this.currentPage, pageSize:9, tid: this.taskId}}).then(res => {
        this.tableData = res.data.records
        this.tableData.filter((item) => {
          if(item.pass === 0)
            item.pass = "打回"
          if(item.pass === 1)
            item.pass = "通过"
        })
      })
    },
  },
}
</script>

<style scoped>

</style>
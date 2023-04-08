<template>
  <div>
    <Header/>
    <div style="margin: 10px 10px; height: 630px; background-color: white">
      <div style="padding-top: 10px; color: #606266; font-size: 25px; text-align: center">我的全部团队</div>
      <!--      表格展示-->
      <div style="padding: 20px">
        <el-table
            :data="tableData"
            border
            stripe
            style="width: 100%">
          <el-table-column
              prop="gid"
              label="团队ID"
              width="500">
          </el-table-column>
          <el-table-column
              prop="groupName"
              label="团队名称"
              width="450">
          </el-table-column>
          <el-table-column
              prop="resp"
              label="角色"
              :filters="[{ text: '负责人', value: '负责人' }, { text: '成员', value: '成员' }]"
              :filter-method="filterResp"
              width="250">
          </el-table-column>
          <el-table-column
              label="操作">
            <template slot-scope="scope">
              <el-button @click="openDetails(scope.row)" type="success" size="small" style="margin-left: 10px">查看</el-button>
              <el-button @click="gotoChat(scope.row)" type="success" size="small" style="margin-left: 25px">团队群聊</el-button>
            </template>
          </el-table-column>
        </el-table>
        <!--        分页-->
        <el-pagination
            @current-change="handleCurrentChange"
            :current-page.sync="currentPage"
            layout="prev, pager, next"
            :total="100"
            style="margin-top: 20px; margin-left: 530px; float: left">
        </el-pagination>
        <div style="float: right; margin-top: 15px">
          <el-button @click="add" size="medium" type="success" style="margin-right: 10px;">创建团队</el-button>
        </div>
      </div>
      <!--      新增弹窗-->
      <el-dialog
          title="新增团队"
          :visible.sync="dialogVisible"
          width="30%"
          :before-close="handleClose">
        <el-form ref="form" :model="form" status-icon :rules="rules" label-width="120px">
          <el-form-item label="团队名称" prop="groupName">
            <el-input v-model="form.groupName" style="width: 80%"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save('form')">确 定</el-button>
        </span>
      </el-dialog>
    </div>
    <Footer/>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import request from "@/utils/request";

export default {
  name: "GroupList",
  components: {Footer, Header},
  data(){
    return{
      user: '',
      tableData: [],
      currentPage: 1,
      dialogVisible: false,
      form: {
        groupName: '',
        uid: '',
        resp: ''
      },
      rules: {
        groupName: [
          {required: true, message: '请输入名称', trigger: 'blur'},
          {max:10, message: '不能超过10位', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("user"));
    this.check()
    this.load()
  },
  methods: {
    check(){
      if(!this.user)
        this.$router.push('/login')
    },
    filterResp(value, row) {
      return row.resp === value;
    },
    handleCurrentChange(val){
      this.currentPage = val;
      this.load()
    },
    openDetails(row){
      this.$router.push({
        path:'/group',
        query:{
          groupKey: row.groupKey
        }
      })
    },
    gotoChat(row){
      this.$router.push({
        path:'/chat',
        query:{
          groupKey: row.groupKey
        }
      })
    },
    load(){
      request.get("/group/load",
          {params:{pageNum: this.currentPage, pageSize:9, uid: this.user.uid}}).then(res => {
        this.tableData = res.data.records;
        this.tableData.filter((item) => {
          if(item.resp === 0)
            item.resp = "成员"
          if(item.resp === 1)
            item.resp = "负责人"
        })
      })
    },
    add(){
      this.dialogVisible = true;
      this.form = {};
    },
    // 创建团队
    save(formName){
      this.$refs[formName].validate((valid) => {
        if(valid){
          this.form.uid = this.user.uid
          this.form.resp = 1
          request.post("/group/add",this.form).then(res => {
            if(res.code === '0'){
              this.$message({
                type: "success",
                message: "添加成功！"
              })
              this.load()
              this.dialogVisible = false
            }else{
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }else{
          alert("请检查！！");
          return false;
        }
      })
    }
  }
}
</script>

<style scoped>
.list:hover{
  cursor: pointer;
}
</style>
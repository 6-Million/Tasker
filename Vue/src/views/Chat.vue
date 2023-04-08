<template>
  <div>
    <Header/>
    <div style="height: 5px"></div>
    <div style="padding: 10px; margin-bottom: 50px">
      <el-row>
        <el-col :span="8">
          <el-card style="width: 80%; margin-left: 30px; min-height: 350px; color: #333; background-color: floralwhite">
            <div style="padding-bottom: 10px; border-bottom: 1px solid #ccc">在线用户</div>
            <div style="padding: 10px 0" v-for="user in users" :key="user.name">
              <span>{{ user.name }}</span>
              <i class="el-icon-chat-dot-round" style="margin-left: 10px; font-size: 16px"></i>
            </div>
          </el-card>
        </el-col>
        <el-col :span="16">
          <div style="width: 950px; margin: 0 auto; background-color: white;
                    border-radius: 5px; box-shadow: 0 0 10px #ccc">
            <div style="text-align: center; line-height: 50px;">
              {{this.groupData.groupName}}
            </div>
            <div style="height: 360px; overflow:auto; border-top: 1px solid #ccc" v-html="content"></div>
            <div style="height: 200px">
            <textarea v-model="text" style="height: 160px; width: 100%; padding: 20px; border: none; border-top: 1px solid #ccc;
             border-bottom: 1px solid #ccc; outline: none"></textarea>
              <div style="text-align: right; padding-right: 10px">
                <el-button type="primary" size="mini" @click="send">发送</el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <Footer/>
  </div>
</template>
<script>
import request from "@/utils/request";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
let socket;
export default {
  name: "Chat",
  components: {Footer, Header},
  data() {
    return {
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      user: {},
      groupKey: '',
      isCollapse: false,
      users: [],
      text: "",
      groupData: [],
      messages: [],
      content: ''
    }
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("user"));
    this.check()
    this.getParams()
    this.init()
  },
  methods: {
    check(){
      if(!this.user)
        this.$router.push('/login')
    },
    getParams(){
      this.groupKey = this.$route.query.groupKey;
    },
    init(){
      request.get("/group/bykey",{params:{key: this.groupKey}}).then(res => {
        this.groupData = res.data
        let name = this.user.name;
        let _this = this;
        if (typeof (WebSocket) == "undefined") {
          console.log("您的浏览器不支持WebSocket");
        } else {
          console.log("您的浏览器支持WebSocket");
          let socketUrl = "ws://localhost:9090/chatserver/" + name + "/" + _this.groupData.gid;
          console.log(socketUrl)
          if (socket != null) {
            socket.close();
            socket = null;
          }
          // 开启一个websocket服务
          socket = new WebSocket(socketUrl);
          //打开事件
          socket.onopen = function () {
            console.log("websocket已打开");
          };
          //  浏览器端收消息，获得从服务端发送过来的文本消息
          socket.onmessage = function (msg) {
            console.log("收到数据====" + msg.data)
            let data = JSON.parse(msg.data)  // 对收到的json数据进行解析， 类似这样的： {"users": [{"name": "zhang"},{ "name": "admin"}]}
            if (data.users) {  // 获取在线人员信息
              _this.users = data.users  // 获取当前连接的所有用户信息，并且排除自身，自己不会出现在自己的聊天列表里
            } else {
              // 如果服务器端发送过来的json数据 不包含 users 这个key，那么发送过来的就是聊天文本json数据
              _this.messages.push(data)
              // 构建消息内容
              _this.createContent(data.from, null, data.text)
            }
          };
          //关闭事件
          socket.onclose = function () {
            console.log("websocket已关闭");
          };
          //发生了错误事件
          socket.onerror = function () {
            console.log("websocket发生了错误");
          }
        }
      })
    },
    send() {
      if (!this.text) {
        this.$message({type: 'warning', message: "请输入内容"})
      } else {
        if (typeof (WebSocket) == "undefined") {
          console.log("您的浏览器不支持WebSocket");
        } else {
          console.log("您的浏览器支持WebSocket");
          // 组装待发送的消息 json
          let message = {from: this.user.name, text: this.text}
          socket.send(JSON.stringify(message));  // 将组装好的json发送给服务端，由服务端进行转发
          this.messages.push({user: this.user.name, text: this.text})
          // 构建消息内容，本人消息
          this.createContent(null, this.user.name, this.text)
          this.text = '';
        }
      }
    },
    createContent(remoteUser, nowUser, text) {  // 这个方法是用来将 json的聊天消息数据转换成 html的。
      let html
      // 当前用户消息
      if (nowUser) { // nowUser 表示是否显示当前用户发送的聊天消息，绿色气泡
        html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
            "  <div class=\"el-col el-col-22\" style=\"text-align: right; padding-right: 10px\">\n" +
            "    <div style='font-size: 4px'>" + nowUser + "</div>\n" +
            "    <div class=\"tip left\">" + text + "</div>\n" +
            "  </div>\n" +
            "  <div class=\"el-col el-col-2\" style=\"margin-top: 15px\">\n" +
            "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
            "    <img src=\"https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png\" style=\"object-fit: cover;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "</div>";
      } else if (remoteUser) {   // remoteUser表示远程用户聊天消息，蓝色的气泡
        html = "<div class=\"el-row\" style=\"padding: 5px 0\">\n" +
            "  <div class=\"el-col el-col-2\" style=\"text-align: right; margin-top: 15px\">\n" +
            "  <span class=\"el-avatar el-avatar--circle\" style=\"height: 40px; width: 40px; line-height: 40px;\">\n" +
            "    <img src=\"https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png\" style=\"object-fit: cover;\">\n" +
            "  </span>\n" +
            "  </div>\n" +
            "  <div class=\"el-col el-col-22\" style=\"text-align: left; padding-left: 10px\">\n" +
            "    <div style='font-size: 4px'>" + remoteUser + "</div>\n" +
            "    <div class=\"tip right\">" + text + "</div>\n" +
            "  </div>\n" +
            "</div>";
      }
      this.content += html;
    },
  }
}
</script>
<style>
.tip {
  color: white;
  text-align: center;
  border-radius: 10px;
  font-family: sans-serif;
  padding: 10px;
  width:auto;
  display:inline-block !important;
  display:inline;
}
.right {
  background-color: wheat;
}
.left {
  background-color: limegreen;
}
</style>

  <template>
  <div>
    <div>
      <el-button type="primary" @click="onSubmit">查询</el-button>
      <el-button @click="dialogVisible = true">上传文件</el-button>
      <el-button type="danger" plain @click="open()">删除</el-button>
    </div>
    <el-form :inline="true" :model="formInline" class="demo-form-inline" style="margin: 20px 0">
      <el-form-item label="审批人">
        <el-input v-model="formInline.user" placeholder="审批人"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="formInline.status" placeholder="状态">
          <el-option label="识别中" value="识别中"></el-option>
          <el-option label="识别结束" value="识别结束"></el-option>
          <el-option label="识别错误" value="识别错误"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item lable="客户名称">
        <span class="demonstration">客户名称</span>
        <el-select v-model="formInline.customid" filterable placeholder="请选择">
          <el-option
            v-for="item in options4"
            :key="item.ecustomid"
            :label="item.customnam"
            :value="item.customname"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <span class="demonstration">时间</span>
        <el-date-picker
          v-model="formInline.date"
          type="daterange"
          align="right"
          unlink-panels
          formagt="yyyy-MM-dd"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions2"
        ></el-date-picker>
      </el-form-item>
    </el-form>
    <el-table
      v-loading="loading2"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
      height:500
      height="500px"
      ref="multipleTable"
      :data="tableData3"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
      border
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" @click="goEdit(scope.$index, scope.row)">
            详情
            <!-- <router-link
              :to="{path:`/DetailPage/:${111 }`,query:{ id:scope.row.resultId }}"
              class="around"
            >详情</router-link> -->
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="create_date" label="创建时间">
        <template slot-scope="scope">{{ scope.row.create_date }}</template>
      </el-table-column>
      <el-table-column prop="custom_no" label="客户业务编号"></el-table-column>
      <el-table-column prop="status" label="状态" show-overflow-tooltip></el-table-column>
    </el-table>
    <el-row class="custrowpage">
      <div class="block pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[20, 40, 80, 100]"
          :page-size="10"
          :current-page="currentPage4"
          layout="total, sizes, prev, pager, next, jumper"
          :total="400"
        ></el-pagination>
      </div>
    </el-row>
    <!-- 上传的digloag -->
    <el-dialog
      title="上传文件"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
      style="text-align:center"
    >
      <div class="demo-input-suffix" style="margin:20px 0">客户名称：
        <el-autocomplete
          class="inline-input"
          v-model="state2"
          :fetch-suggestions="querySearch"
          placeholder="请输入内容"
          :trigger-on-focus="false"
          @select="handleSelect"
        ></el-autocomplete>
      </div>

      <el-upload
        class="upload-demo"
        drag
        action="https://jsonplaceholder.typicode.com/posts/"
        multiple
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

  <script>
export default {
  name: "todo",
  data() {
    return {
      multipleTable: [],
      multipleSelection: [],
      dialogVisible: false,
      tableData3: [],
      options4: [],
      loading2: false,
      state2: "",
      formInline: {
        user: "",
        status: "",
        date: "",
        customid: ""
      },
      options4: [],
      value9: [],
      list: [],
      loading: false,
      states: [],
      status1: "",
      createDate: "",
      customNo: "",
      currentPage4: 4,
      offset: 1,
      limit: 20,
      pickerOptions2: {
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            }
          }
        ]
      },
      value6: "",
      value7: ""
    };
  },
  mounted: function() {
    this.getTable(
      this.status1,
      this.createDate,
      this.customNo,
      this.offset,
      this.limit
    );
    this.getSelect();
    this.list = this.states.map(item => {
      return { value: item, label: item };
    });
  },
  methods: {
    querySearch(queryString, cb) {
      var restaurants = this.restaurants;
      var results = queryString
        ? restaurants.filter(this.createFilter(queryString))
        : restaurants;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return restaurant => {
        return (
          restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) ===
          0
        );
      };
    },
    loadAll() {
      return [
        { value: "三全鲜食（北新泾店）", address: "长宁区新渔路144号" },
        {
          value: "Hot honey 首尔炸鸡（仙霞路）",
          address: "上海市长宁区淞虹路661号"
        },
        {
          value: "新旺角茶餐厅",
          address: "上海市普陀区真北路988号创邑金沙谷6号楼113"
        },
        { value: "泷千家(天山西路店)", address: "天山西路438号" },
        {
          value: "胖仙女纸杯蛋糕（上海凌空店）",
          address: "上海市长宁区金钟路968号1幢18号楼一层商铺18-101"
        },
        { value: "贡茶", address: "上海市长宁区金钟路633号" },
        {
          value: "豪大大香鸡排超级奶爸",
          address: "上海市嘉定区曹安公路曹安路1685号"
        },
        {
          value: "茶芝兰（奶茶，手抓饼）",
          address: "上海市普陀区同普路1435号"
        },
        { value: "十二泷町", address: "上海市北翟路1444弄81号B幢-107" },
        { value: "星移浓缩咖啡", address: "上海市嘉定区新郁路817号" },
        { value: "阿姨奶茶/豪大大", address: "嘉定区曹安路1611号" },
        { value: "新麦甜四季甜品炸鸡", address: "嘉定区曹安公路2383弄55号" },
        {
          value: "Monica摩托主题咖啡店",
          address: "嘉定区江桥镇曹安公路2409号1F，2383弄62号1F"
        },
        {
          value: "浮生若茶（凌空soho店）",
          address: "上海长宁区金钟路968号9号楼地下一层"
        },
        { value: "NONO JUICE  鲜榨果汁", address: "上海市长宁区天山西路119号" },
        { value: "CoCo都可(北新泾店）", address: "上海市长宁区仙霞西路" },
        {
          value: "快乐柠檬（神州智慧店）",
          address: "上海市长宁区天山西路567号1层R117号店铺"
        },
        {
          value: "Merci Paul cafe",
          address: "上海市普陀区光复西路丹巴路28弄6号楼819"
        },
        {
          value: "猫山王（西郊百联店）",
          address: "上海市长宁区仙霞西路88号第一层G05-F01-1-306"
        },
        { value: "枪会山", address: "上海市普陀区棕榈路" },
        { value: "纵食", address: "元丰天山花园(东门) 双流路267号" },
        { value: "钱记", address: "上海市长宁区天山西路" },
        { value: "壹杯加", address: "上海市长宁区通协路" },
        {
          value: "唦哇嘀咖",
          address: "上海市长宁区新泾镇金钟路999号2幢（B幢）第01层第1-02A单元"
        },
        { value: "爱茜茜里(西郊百联)", address: "长宁区仙霞西路88号1305室" },
        {
          value: "爱茜茜里(近铁广场)",
          address:
            "上海市普陀区真北路818号近铁城市广场北区地下二楼N-B2-O2-C商铺"
        },
        {
          value: "鲜果榨汁（金沙江路和美广店）",
          address: "普陀区金沙江路2239号金沙和美广场B1-10-6"
        },
        {
          value: "开心丽果（缤谷店）",
          address: "上海市长宁区威宁路天山路341号"
        },
        { value: "超级鸡车（丰庄路店）", address: "上海市嘉定区丰庄路240号" },
        { value: "妙生活果园（北新泾店）", address: "长宁区新渔路144号" },
        { value: "香宜度麻辣香锅", address: "长宁区淞虹路148号" },
        {
          value: "凡仔汉堡（老真北路店）",
          address: "上海市普陀区老真北路160号"
        },
        { value: "港式小铺", address: "上海市长宁区金钟路968号15楼15-105室" },
        { value: "蜀香源麻辣香锅（剑河路店）", address: "剑河路443-1" },
        { value: "北京饺子馆", address: "长宁区北新泾街道天山西路490-1号" },
        {
          value: "饭典*新简餐（凌空SOHO店）",
          address: "上海市长宁区金钟路968号9号楼地下一层9-83室"
        },
        {
          value: "焦耳·川式快餐（金钟路店）",
          address: "上海市金钟路633号地下一层甲部"
        },
        { value: "动力鸡车", address: "长宁区仙霞西路299弄3号101B" },
        { value: "浏阳蒸菜", address: "天山西路430号" },
        { value: "四海游龙（天山西路店）", address: "上海市长宁区天山西路" },
        {
          value: "樱花食堂（凌空店）",
          address: "上海市长宁区金钟路968号15楼15-105室"
        },
        { value: "壹分米客家传统调制米粉(天山店)", address: "天山西路428号" },
        {
          value: "福荣祥烧腊（平溪路店）",
          address: "上海市长宁区协和路福泉路255弄57-73号"
        },
        {
          value: "速记黄焖鸡米饭",
          address: "上海市长宁区北新泾街道金钟路180号1层01号摊位"
        },
        { value: "红辣椒麻辣烫", address: "上海市长宁区天山西路492号" },
        {
          value: "(小杨生煎)西郊百联餐厅",
          address: "长宁区仙霞西路88号百联2楼"
        },
        { value: "阳阳麻辣烫", address: "天山西路389号" },
        {
          value: "南拳妈妈龙虾盖浇饭",
          address: "普陀区金沙江路1699号鑫乐惠美食广场A13"
        }
      ];
    },
    handleSelect(item) {
      console.log(item);
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.getTable(this.status1, this.createDate, this.customNo, val, 20);
    },
    open() {
      this.$notify.error({
        title: "错误",
        message: "这是一条错误的提示消息"
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    goEdit(index, row) {
      console.log(index);
      console.log(row);

      this.$router.push({
        path: "/DetailPage",
        name: "DetailPage",
        params: row
        /*query: {  
                key: 'key',   
                msgKey: this.msg  
            }*/
      });
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },
    getTable(status, createDate, customNo, offset, limit) {
      const tthis = this;
      console.log(
        `/apis/jintie/resultList?status=${status}&createDate=${createDate}&CustNo=${customNo}&offset=${offset}&limit=${limit}`
      );
      this.$http
        .get(
          `/apis/jintie/resultList?status=${status}&createDate=${createDate}&CustNo=${customNo}&offset=${offset}&limit=${limit}`
        )
        .then(function(response) {
          tthis.tableData3 = response.data.resultList;
          console.log(tthis.tableData3);
        })
        .catch(function(error) {
          console.log(error);
        });
    },
    getSelect() {
      const tthis = this;
      tthis.loading2= true,
      this.$http 
        .get("/apis/jintie/searchCustom")
        .then(function(response) {
          console.log(response);
          tthis.options4 = response.data;
          tthis.loading2= false
        })
        .catch(function(error) {
          tthis.loading2= false
          console.log(error);
        }); 
    },
    onSubmit() {
      console.log(this.moment(this.formInline.date[0]).format("YY-MM-DD"));
      console.log(this.formInline.status);
      this.getTable(
        this.formInline.status,
        this.formInline.date,
        this.formInline.user,
        1,
        20
      );
    }
  }
};
</script>

<style>
.pagination {
  text-align: right;
  margin-top: 10px;
}
</style>

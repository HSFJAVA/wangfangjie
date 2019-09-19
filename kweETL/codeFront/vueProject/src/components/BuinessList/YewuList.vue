  <template>
  <div style="height:100%">
    <div>
      <el-button type="primary" plain @click="onSubmit" icon="el-icon-search">查询</el-button>
      <el-button type="primary" plain @click="editYw()">修改</el-button>
      <el-button type="primary" plain @click="dialogVisible = true">
        上传文件
        <i class="el-icon-upload el-icon--right"></i>
      </el-button>
      <!-- <el-button @click="dialogVisible = true">数据导出</el-button> -->
      <el-button type="primary" plain @click="dialogFormVisibleNew = true">创建业务</el-button>
      <!-- <el-button @click="dialogVisible = true">单证打印</el-button> -->
      <el-button type="danger" plain @click="deleteTab()">删除</el-button>
    </div>
    <el-form :inline="true" :model="formSearch" class="demo-form-inline" style="margin: 20px 0">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="业务编号">
            <el-input v-model="formSearch.onlyNo" placeholder="业务编号" size="mini"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="唯一编号">
            <el-input v-model="formSearch.yewuNo" placeholder="唯一编号" size="mini"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="客户名称">
            <el-select
              v-model="formInline.custName"
              filterable
              placeholder="请选择"
              style="width:100%"
              size="mini"
            >
              <el-option
                v-for="item in options4"
                :key="item.ecustomid"
                :label="item.customName"
                :value="item.customName"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="创建人">
            <el-input v-model="formSearch.creater" placeholder="创建人" size="mini"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="业务状态">
            <!-- <el-input v-model="formSearch.yewuState" placeholder="业务状态" size="mini"></el-input> -->
             <el-select v-model="formSearch.yewuState" clearable placeholder="请选择" size="mini">
              <el-option
                v-for="item in startType"
                :key="item.label"
                :label="item.label"
                :value="item.label"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="6">
          <el-form-item label="接单备注">
            <el-input v-model="formSearch.jiedanRemark" placeholder="接单备注" size="mini"></el-input>
          </el-form-item>
        </el-col> -->
        <el-col :span="6">
          <el-form-item label="单证类型">
            <!-- <el-input v-model="formSearch.yewuType" placeholder="单证类型" size="mini"></el-input> -->
            <el-select v-model="formSearch.yewuType" clearable placeholder="请选择" size="mini">
              <el-option
                v-for="item in optionType"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="10">
          <el-form-item>
            <span class="demonstration">创建时间</span>
            <el-date-picker
              v-model="formSearch.date"
              type="daterange"
              align="right"
              unlink-panels
              formagt="yyyy-MM-dd"
              size="mini"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions2"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-table
      v-loading="loading"
      element-loading-text="拼命加载中"
      height:500
      height="500px"
      ref="multipleTable"
      :data="tableData3"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
      border
      :highlight-current-row="true"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column type="index" width="50" fixed></el-table-column>
      <el-table-column label="操作" width="80px" fixed>
        <template slot-scope="scope">
          <el-button type="success" plain size="mini" @click="goEdit(scope.$index, scope.row)">详情</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="类型" width="80px"></el-table-column>
      <el-table-column prop="customNo" label="客户业务编号" width="160px"></el-table-column>
      <el-table-column prop="clearingCustomerName" label="客户名称" width="180px"></el-table-column>
      <el-table-column prop="ywNo" label="唯一编号" width="280px"></el-table-column>
      <el-table-column prop="status" label="状态" show-overflow-tooltip width="80px">
        <template slot-scope="scope">
          <span v-if="scope.row.status == '未录入'" style="color:red">{{ scope.row.status }}</span>
          <span v-else style="color: #606266">{{ scope.row.status }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="createUserEmp" label="创建人" width="100px"></el-table-column>
      <el-table-column prop="create_date" label="创建时间" width="180px">
        <template slot-scope="scope">{{moment(scope.row.createDateTime).format("YYYY-MM-DD HH:MM:SS")}}</template>
      </el-table-column>
      <el-table-column prop="updateUserEmp" label="更新人" width="100px"></el-table-column>
            <el-table-column prop="update_date" label="更新时间" width="180px">
        <template slot-scope="scope">{{moment(scope.row.updateDate).format("YYYY-MM-DD HH:MM:SS")}}</template>
      </el-table-column>
      <el-table-column prop="auditUserEmp" label="审核人" width="100px"></el-table-column>

      <el-table-column prop="remark" label="备注" show-overflow-tooltip width="180px"></el-table-column>
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
      <div class="demo-input-suffix" style="margin:20px 0">
        客户名称：
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

    <!-- 新建单证的dialogvisible -->
    <el-dialog title="新建业务" :visible.sync="dialogFormVisibleNew" width="35%">
      <el-form :model="Newyewu" label-width="140px">
        <el-form-item label="客户名称">
          <el-select
            v-model="formInline.clearingCustomerName"
            filterable
            placeholder="请选择"
            style="width:100%"
          >
            <el-option
              v-for="item in options4"
              :key="item.ecustomid"
              :label="item.customName"
              :value="item.customName"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="结算客户编号">
          <el-input v-model="formInline.customNo" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="单证类型">
          <el-select v-model="formInline.type" placeholder="单证类型" style="width:100%">
            <el-option label="进仓" value="0"></el-option>
            <el-option label="出仓" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="接单备注">
          <el-input v-model="formInline.remark" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editcencal()">取 消</el-button>
        <el-button type="primary" @click="submitNewYewu()">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 这是编辑的dialogvislable -->
    <el-dialog title="修改內容" :visible.sync="dialogFormVisibleEdit" center width="30%">
      <el-form :model="formYwEdit">
        <el-form-item label="业务编号" :label-width="formLabelWidth">
          <el-input v-model="formYwEdit.customNo" auto-complete="off"></el-input>
        </el-form-item> 
        <el-form-item label="备注" :label-width="formLabelWidth">
          <el-input v-model="formYwEdit.remark" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisibleEdit = false">取 消</el-button>
        <el-button type="primary" @click="eEditYw()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

  <script>
import { open } from "fs";
export default {
  name: "todo",
  data() {
    return {
      multipleTable: [],
      multipleSelection: [],
      dialogVisible: false,
      dialogFormVisibleNew: false,
      dialogFormVisibleEdit: false,
      tableData3: [],
      session: "",
      formLabelWidth: "80px",
      loading: false,
      state2: "",
      formYwEdit: {},
      edittable: false,
      optionType: [
        {
          value: 0,
          label: "进仓"
        },
        {
          value: 1,
          label: "出仓"
        }
      ],
       startType: [
        {
          value: 1,
          label: "待审核"
        },
        {
          value: 2,
          label: "已审核"
        },
        {
          value: 3,
          label: "已同步"
        },
        {
          value: 4,
          label: "已完成"
        },
        {
          value: 5,
          label: "已修改"
        },
        {
          value: 6,
          label: "待修改"
        }
      ],
      formInline: {
        clearingCustomerName: "",
        remark: "",
        type: "",
        customNo: "",
        createUser: ""
      },
      Newyewu: {
        custname: ""
      },
      formSearch: {
        onlyNo: "",
        yewuNo: "",
        custName: "",
        creater: "",
        date: "",
        yewuState: "",
        yewuType: "",
        jiedanRemark: ""
      },
      yewuState:'',
      options4: [],
      value9: [],
      list: [],
      creater:'',
      loading: false,
      states: [],
      status1: "",
      createDate: "",
      custNo: "",
      op_user: "",
      startDate: "",
      endDate: "",
      ywNo: "",
      currentPage4: 1,
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
    this.getSelect();
    this.list = this.states.map(item => {
      return { value: item, label: item };
    });
    this.getsession();
        this.getTable(
        this.formSearch.yewuState,
        this.formSearch.date,
        this.formSearch.onlyNo,
        this.formSearch.yewuNo,
        1,
        20,
        this.formSearch.creater,
        this.formSearch.yewuType,
        1
    );
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
    getsession() {
      if (sessionStorage.getItem("name")) {
        this.session = sessionStorage.getItem("name");
        this.formSearch.creater = this.session;
      } else {
        this.$router.push("/");
      }
    },
    editRoll(index, row) {
      console.log(index);
      console.log(row);
      this.edittable = true;
    },
    handleSelect(item) { 
      console.log(item);
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.getTable(
        // this.status1,
        // this.startDate,
        // this.custNo,
        // this.ywNo,
        // 1,
        // val,
        // this.creater
        this.formSearch.yewuState,
        this.formSearch.date,
        this.formSearch.onlyNo,
        this.formSearch.yewuNo,
        1,
        val,
        this.formSearch.creater,
        this.formSearch.yewuType,
        1
      );
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.getTable(
        this.formSearch.yewuState,
        this.formSearch.date,
        this.formSearch.onlyNo,
        this.formSearch.yewuNo,
        val,
        20,
        this.formSearch.creater,
        this.formSearch.yewuType,
        1
      );
    },
    deleteTab() {
      const tthis = this;
      if (this.multipleSelection.length < 1) {
        this.$message.error("请选择一条数据进行操作！");
      } else {
        const dataArr = [];
        for(let i=0; i<this.multipleSelection.length; i++) {
           if (this.multipleSelection[i].status !== "待审核" && !this.multipleSelection[i].status) {
            tthis.$message({
              message: "只能删除审核前单证",
              type: "error"
            });
            return;
          } else {
            dataArr.push(this.multipleSelection[i].ywNo);
          }
        }
        const deInfo = {
          ywlist: dataArr,
          opUser: this.session
        };
        this.$http.post(`/apis/kwe/index/ywDrop`, deInfo).then(resp => {
          console.log(resp);
          if (resp.data == "success") {
            tthis.getTable(
                tthis.formSearch.yewuState,
                tthis.formSearch.date,
                tthis.formSearch.onlyNo,
                tthis.formSearch.yewuNo,
                1,
                20,
                tthis.formSearch.creater,
                tthis.formSearch.yewuType,
                1
            );
             tthis.$message({
              showClose: true,
              message: "删除成功",
              type: "success"
            });
          } else {
            tthis.$message({
              message: "删除失败",
              type: "error"
            });
          }
        });
      }
    },
    editYw() {
      const tthis = this;
      if (this.multipleSelection.length != 1) {
        this.$message.error("请选择一条数据进行操作！");
      } else {
        this.dialogFormVisibleEdit = true;
        this.formYwEdit = this.multipleSelection[0];
        this.formYwEdit.updateUser = this.session;
      }
    },
    eEditYw() {
      const tthis = this;
      console.log(this.formYwEdit);
      if (this.formYwEdit.type == "进仓") {
        this.formYwEdit.type = 0;
      } else {
        this.formYwEdit.type = 1;
      }
      this.$http
        .post(`/apis/kwe/index/updateYW`, this.formYwEdit)
        .then(function(res) {
          console.log(res);
          if (res.data == "updateNice") {
            tthis.getTable;
            tthis.$message({
              showClose: true,
              message: "修改成功",
              type: "success"
            });
            tthis.dialogFormVisibleEdit = false;
            tthis.getTable(
                this.formSearch.yewuState,
                this.formSearch.date,
                this.formSearch.onlyNo,
                this.formSearch.yewuNo,
                1,
                20,
                this.formSearch.creater,
                this.formSearch.yewuType,
                1
            );
          } else {
            tthis.$message({
              message: "修改失败",
              type: "error"
            });
          }
        });
    },
    editcencal() {
       this.dialogFormVisibleNew = false 
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    goEdit(index, row) {
      if (row.type == "出仓") {
        this.$router.push({
          path: "/exportenter",
          name: "exportenter",
          params: row,
          query: row
        });
      } else if (row.type == "进仓") {
        this.$router.push({
          path: "/importenter",
          name: "importenter",
          params: row,
          query: row
        });
      }
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },
    getTable(status, startDate, customNo, ywNo, offset, limit, op_user,type,remark10) {
      const tthis = this;
      this.$http
        .get(
          `/apis/kwe/index/ywList?status=${status}&dateStr=${startDate}&customNo=${customNo}&ywNo=${ywNo}&current=${offset}&size=${limit}&createUser=${op_user}&type=${type}&remark10=${remark10}`
        )
        .then(function(response) {
          console.log(response);
          tthis.tableData3 = response.data.ywList.records;
          tthis.tableData3.forEach(res => {
            if (res.type == 0) {
              res.type = "进仓";
            } else {
              res.type = "出仓";
            }
          });
        })
        .catch(function(error) {
          console.log(error);
        });
    },
    getSelect() {
      const tthis = this;
      (tthis.loading = true),
        this.$http
          .get("/apis/kwe/index/searchCustom")
          .then(function(response) {
            console.log(response);
            tthis.options4 = response.data;
            tthis.loading = false;
          })
          .catch(function(error) {
            tthis.loading = false;
            console.log(error);
          });
    },
    onSubmit() { 
      // console.log(this.moment(this.formSearch.date[0]).format("YY-MM-DD"));
      var searchDate = "";
      if (this.formSearch.date[0]) {
        searchDate = `${this.moment(this.formSearch.date[0]).format(
          "YYYY-MM-DD"
        )}||${this.moment(this.formSearch.date[1]).format("YYYY-MM-DD")}`;
      } else {
        searchDate = "";
      }
      console.log(searchDate);
      console.log(this.formSearch);
      this.getTable(
        this.formSearch.yewuState,
        searchDate,
        this.formSearch.onlyNo,
        this.formSearch.yewuNo,
        1,
        20,
        this.formSearch.creater,
        this.formSearch.yewuType,
        1
      );
    },
    submitNewYewu() {
      console.log(this.formInline);
      this.formInline.createUser = this.session;
      var searchDate = "";
      if (this.formSearch.date[0]) {
        searchDate = `${this.moment(this.formSearch.date[0]).format(
          "YYYY-MM-DD"
        )}||${this.moment(this.formSearch.date[1]).format("YYYY-MM-DD")}`;
      } else {
        searchDate = "";
      }
      const tthis = this;
      this.$http
        .post("/apis/kwe/index/addYW", this.formInline)
        .then(function(resp) {
          tthis.getTable(
                tthis.formSearch.yewuState,
                tthis.formSearch.date,
                tthis.formSearch.onlyNo,
                tthis.formSearch.yewuNo,
                1,
                20,
                tthis.formSearch.creater,
                tthis.formSearch.yewuType,
                1
          );
          tthis.dialogFormVisibleNew = false;
          tthis.$message({
            message: "新建业务成功",
            type: "success"
          });
        })
        .catch(function(error) {
          console.log(error);
          tthis.$message({
            message: "创建失败",
            type: "error"
          });
        });
    }
  }
};
</script>

<style>
html,
body {
  height: 100%;
}
.pagination {
  text-align: right;
  margin-top: 10px;
}
</style>

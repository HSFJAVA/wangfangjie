<template>
  <div>
    <div>
        <el-table 
            v-loading="loading"
            element-loading-text="拼命加载中"
            :data="ocrBaseTable"
            stripe
            style="width: 100%;height:100%"
            border
        >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column type="index" width="50"></el-table-column>
            <el-table-column label="操作" width="150">
            <template slot-scope="scope">
                          <el-button type="success" plain size="mini" @click="goEdit(scope.$index, scope.row)">详情</el-button>
            </template>
            </el-table-column>
            <!-- <el-table-column prop="parameter" label="原始识别JSON"></el-table-column> -->
        </el-table>
        <el-row class="custrowpage">
            <div class="block pagination">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :page-sizes="[10,20, 40, 80, 100]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :current-page="currentPage"
                    :total="total"
                ></el-pagination>
            </div>
        </el-row>
    </div>
  </div>
</template>
<script>
  export default {
    name:"ocrBaseData",
    data() {
      return {
        loading: false,
        total:0,
        pageSize:10,
        currentPage:0,
        ocrBaseTable:[]
      };
    },
    mounted:function() {
        this.initTable();
    },
    methods: {
      handleSizeChange(val){
        const tthis = this;
        tthis.pageSize = val;
        tthis.initTable();
      },
      handleCurrentChange(val){
        const tthis = this;
        tthis.currentPage = val;
        tthis.initTable();
      },
      initTable() {
        const tthis = this;
        tthis.loading = true;
        const data = {
            currentPage:tthis.currentPage,
            pageSize:tthis.pageSize
        }
        console.log(data);
        this.$http.post('/apis/kwe/index/getOcrBaseData',data)
        .then(function(response){
            console.log(response.data);
            tthis.ocrBaseTable = response.data.records;
            tthis.total = response.data.total;
            tthis.loading = false;
        })
      }
    }
  };
</script>
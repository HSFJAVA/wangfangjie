export default {
    searchparm(custtype, custval ) {
        this.$http
        .get(`/apis/jintie/selectCode?dictType=2`)
        .then(function(resp) {
          console.log("测试的code");
          console.log(resp);
        });
    }
}
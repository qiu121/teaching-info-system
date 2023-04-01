function handleSubmit(data) {
    this.$refs.submitFormData.validate((valid) => {
        if (valid) {
            axios.post("/feedback/add", data)
                .then((response) => {
                        if (response.data.code === 20011) {
                            this.$message.success(response.data)
                            window.location.href = "../../../login/index.html";
                        } else {
                            this.$message({
                                message: (response.data.msg),
                                type: 'error',
                                center: true
                            })
                        }
                    }
                ).catch((e) => {
                this.$message({
                    message: ("接口请求异常  " + e),
                    type: 'error',
                    center: true
                })
            });
        }
    })
}
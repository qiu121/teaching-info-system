function updateTApi(data, old) {
    return axios({
        method: 'put',
        url: '/users/stu/update/' + old,
        data: data
    })
}

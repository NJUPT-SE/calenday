<template>
	<!-- 本页面 -->
	<view class="bg-white">
		<!-- 功能按钮st -->
		<view class="flex padding-top padding-right justify-end">
			<button class="cu-btn light bg-orange shadow-lg round" @tap="createBigDay">
				<text class="cuIcon-add"></text> 
			</button>
		</view>
		<!-- 功能按钮ed -->

		<!-- 时间轴st -->
		<view class="padding">
			<view class="cu-timeline" v-for="dayInfo in bigDays" :key="dayInfo.id">

				<!-- 日期 -->
				<view class="cu-time">{{dayInfo.date.substring(dayInfo.date.length - 5)}}</view>

				<!-- 图标 -->
				<view class="cu-item" :class="'text-'+colorList[dayInfo.img%9]">

					<!-- 内容 -->
					<view class="content light" :class="'bg-'+colorList[dayInfo.img%9]" @longpress="del(dayInfo.id)" data-target="DialogModal">
						<text>【{{dayInfo.title}}】{{dayInfo.notes}}</text>
					</view>
				</view>


			</view>
		</view>

		<!-- 时间轴ed -->

		<!-- 删除弹框st -->
		<view class="cu-modal" :class="modalName=='DialogModal'?'show':''">
			<view class="cu-dialog">
				<view class="cu-bar bg-white justify-end">
					<view class="content text-red">提醒</view>
					<view class="action" @tap="hideModal">
						<text class="cuIcon-close text-red"></text>
					</view>
				</view>
				<view class="padding-xl">
					您要将该回忆...？
					<!-- 传给后端的值：uid={{uid}} id={{selectedId}} index={{selectedIndex}} -->
				</view>
				<view class="cu-bar bg-white col-2">
					<view class="padding">
						<button class="cu-btn bg-green " @tap="comfirmEdit">回炉重造</button>
					</view>
					<view class="padding">
						<button class="cu-btn bg-red margin-left" @tap="comfirmDel">忍痛丢弃</button>
					</view>
				</view>
			</view>
		</view>

		<!-- 删除弹框ed -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				uid: 1,
				modalName: null,
				colorList: ["red", "orange", "yellow", "olive", "green", "cyan", "blue", "purple", "mauve", "pink"],
				bigDays: [],
				selectedId: -1,
			}
		},
		methods: {
			//获取用户id
			getUid: function() {
				const _this = this;
				//获取id
				wx.login({
					success(res) {
						if (res.code) {
							//发起网络请求
							const secret = res.code;
							let uinfo = {};
							wx.getUserInfo({
								success: function(res) {
									uinfo = res.userInfo;
									uni.request({
										method: "GET",
										url: "http://localhost:8080/api/UserManage",
										header: {
											"content-type": "application/json"
										},
										data: {
											code: secret,
											gender: uinfo.gender,
											nickname: uinfo.nickName,
											avaUrl: uinfo.avatarUrl
										},
										success: (res) => {
											_this.uid = res.data.uid;
											_this.getBigDays();
											// console.log(_this.uid);
										},
										fail: (res) => {
											// console.log(res);
											console.log('登录失败!');
										}
									})
								}
							});
						} else {
							console.log('登录失败！' + res.errMsg)
						}
					}
				});

			},

			//获取bigdays
			getBigDays: function() {
				//根据id获取清单
				// console.log(this.uid);
				uni.request({
					method: 'GET',
					url: 'http://localhost:8080/api/bigDay/view',
					data: {
						uid: this.uid,
					},
					success: (res) => {
						if (res.data.err == 1) {
							// console.log(res);
							this.bigDays = res.data.data;
						} else {
							console.log(res);
							console.log("Bad request.");
						}

					},
					fail: () => {
						console.log("Request failed.");
					}
				})
			},
			createBigDay: function() {
				uni.navigateTo({
					animationType: "slide-in-right",
					url: "./createBigDay"
				})
			},
			showModal: function(e) {
				this.modalName = "DialogModal";
			},

			hideModal: function(e) {
				this.modalName = null
			},
			del: function(dateId) {
				this.showModal();
				this.selectedId = dateId;
			},
			comfirmEdit: function() {
				console.log("edit: ", this.selectedId);
				this.hideModal();
				uni.navigateTo({
					url:'./editeBigDay?uid='+this.uid+'&id='+this.selectedId
				})
			},
			comfirmDel: function() {
				const _this = this;
				// console.log("delete: ", this.selectedId);
				uni.request({
					method: 'GET',
					url: 'http://localhost:8080/api/bigDay/delete',
					data: {
						uid: _this.uid,
						id: _this.selectedId
					},
					success: (res) => {
						if (res.data.err == 1){
							this.getUid();
						}
					}
				})
				this.hideModal();
			},
		},
		mounted: function() {
			this.$nextTick(function() {
				this.getUid();
			})
		},
		onPullDownRefresh() {
			//监听下拉刷新动作的执行方法，每次手动下拉刷新都会执行一次
			this.getUid();
		}
	}
</script>

<style>

</style>

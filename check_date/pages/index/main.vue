<template>
	<view>
		<view class="main_park">
			<view>
				<text class="card_part_mesg">{{card_part_mesg_text}}</text>
			</view>
			<!--打卡界面-->
			<view class="show_card" v-for="iterm in img_object">
				<view class="crad_image">
					<image :src=iterm.img mode="aspectFit" @click="click_img"></image>
					<text>{{iterm.title}}</text>
				</view>
			</view>
		</view>
		<!--悬浮按钮-->
		<change_fab></change_fab>
	</view>
</template>

<script>
	import change_fab from '@/components/change_fab/change_fab.vue'
	export default {
		/*悬浮按钮数据段*/
		components: {
			change_fab,
		},

		data() {
			return {


				//card_part_mesg_text:"晨间",
				user_id: 0,

				/*调试使用*/
				//card_id:1,
				//title:"游泳",
				//img1:"../../static/card_icons/png/41.png",
				//img2:"../../static/card_icons/png/40.png",

				img_object: [{}],
				/*img_object
				[{
					"id":
					"title":
					"img":
				},]
				*/
			}
		},

		onLoad: function(e) {
			get_info(e)
		},

		methods: {
			/*打开界面获取信息*/
			get_info(e) {
				uni.request({
					url: 'http://localhost/api/habit/present',
					method: 'GET',
					data:{
						uid:this.user_id
					},
					/*返回值*/
					success: (res) => {
						/*错误接收暂且不管*/
						// err = res.err,
						this.user_id = res.uid,
						this.img_object = res.data
					}
				})
			},

			/*点击打卡*/
			click_img() {
				uni.request({
					url: 'http://localhost/api/habit/clockin',
					method: 'GET',
					data: {
						uid: get_info.user_id,
						id: get_info.user_card.card_id
					},
					success(e) {
						console.log("打卡状态" + e);
					}
				})
			},
		}
	}
</script>

<style>
	.crad_image {
		width: 180rpx;
		height: 180rpx;
		//line-height: 230rpx;
		padding: 20rpx;
	}

	.show_card {
		flex-direction: row;
		flex-wrap: wrap;
		justify-content: space-around;
		align-content: flex-start;
		flex: auto |[ <'flex-grow'> <'flex-shrink'>? || <'flex-basis'>]
	}
</style>

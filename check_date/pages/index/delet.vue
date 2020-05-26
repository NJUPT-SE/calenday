<template>
	<view>
		<view class="main_park">
			<view>
				<text class="card_part_mesg">{{card_part_mesg_text}}</text>
			</view>
			<!--删除界面-->
			<view class="show_card" v-for="iterm in img_object">
				<view class="crad_image">
					<image :src=iterm.img mode="aspectFit" @click="click_img"></image>
					<text>{{iterm.title}}</text>
				</view>
			</view>
		</view>
	
	</view>
</template>

<script>
	export default {
		data() {
			return {
				user_id: 0,
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
			
		},

		methods: {
			/*打开获取和主界面相同信息*/
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

			/*点击删除*/
			click_img() {
				uni.request({
					url:'http://localhost/api/habit/delete',
					method:'GET',
					data:{
						uid:this.user_id,
						id:this.img_object.id
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

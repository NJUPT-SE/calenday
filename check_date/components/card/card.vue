<template>
	<view>
		<image class="crad_image" :src=get_info.img mode="aspectFit" @click="click_img"></image>
	</view>
</template>

<script>
	export default{
		props:{
			get_info:{
				type:Object,
				default:''
			}
		},
		data(){
			
		},
		methods:{
			/*点击打卡*/
			// click_img(){
			// 	uni.request({
			// 		url:'http://localhost/api/habit/clockin',
			// 		method:'POST'
			// 		data:{
			// 			uid:get_info.user_id,
			// 			id:get_info.user_card.card_id
			// 		},
			// 		success(e) {
			// 			console.log("打卡状态"+e);
			// 		}
			// 	})
			// }
		}
	}
</script>

<style>
	.crad_image{
		width: 200rpx;
		height: 200rpx;
		line-height: 230rpx;
		border-bottom: 1px #F5F5F5 solid;
	}
	
</style>

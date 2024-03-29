 function change(result){
	for (var i = result.contentList.length - 1; i >= 0; i--) {
		/* console.log(data.contentList[i].cntntThumnail); */
		var Thumnail = result.contentList[i].cntntThumnail;
		var Title = result.contentList[i].cntntTitle;
		var cntntId = result.contentList[i].cntntId;
		var scroll = `
			  <img src="`+ Thumnail + `" class="card-img-top" alt="">
			  <div class="card-body">
			    <p class="card-title card-text text-center">` + Title + `</p>
			  </div>
		`;
		
		var divEl = document.createElement("div");
		divEl.setAttribute("class", "card mx-3 my-3");
		divEl.setAttribute("style", "width: 18rem;");
		divEl.setAttribute("cntntId", cntntId);
		divEl.setAttribute("onclick", "cntntDetail(this.getAttribute('cntntId'))");
		divEl.innerHTML = scroll;
		document.querySelector("#contentsList").appendChild(divEl);
		}
}
	$(document).ready(function() {
		var start = 1;
		var end = 15;
		
		var cate = "[[${cate}]]";
		
		
		$(window).scroll(function() {
			if ( Math.round($(window).scrollTop()) == $(document).height() - $(window).height() ) {
				start = end + 1;
				end = start + 4;
				$.ajax({
					type: 'GET',
					url: '/content/scroll',
					data: {start : start, end : end},
					success: function(result) {
						change(result);
					}
				}) 
			}
		})
	})
$(document).ready(function () {
    var requestData = {
        trendQueryList: document.getElementById("trendQueryList").value.slice(1, -1),
        cntntId: document.getElementById("cntntId").value
    };
    // 추천 콘텐츠
    $.ajax({
        url: '/cs/content/keyword', type: 'GET',
        data: requestData,
        success: function (contentList) {
            const element = document.getElementById('card-list');
            for (i = 0; i < contentList.length; i++) {
                inHtml = `
            <li class="card-item" id="card-item" onclick="recomCntntDetail(${contentList[i].cntntId})">
                <figure class="card-image"style="background-image: url(${contentList[i].cntntThumnail})">
                    <img src=${contentList[i].cntntThumnail} alt="일분이">
                </figure>
                <div class="card-desc">
                   ${contentList[i].cntntTitle}
                </div>
            </li>`;
                element.insertAdjacentHTML("afterbegin", inHtml);
            }
        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });

    // 키워드로 인스타 크롤링
    $.ajax({
        url: '/cs/insta-img', type: 'GET',
        data: requestData,
        success: function (data) {
            console.log(data);
            const element = document.getElementById('trend');
            data.slice(1, -1);
            data.slice(1, -1);
            element.innerHTML = data
        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });

    var requestData = {
        targetContentIdF: document.getElementById("contentId").value
    };

    $.ajax({
        url: '/cs/youtube/iframe', type: 'GET', data: requestData, // 데이터 객체 전달
        success: function (data2) {
            // 서버에서 받은 데이터를 result 요소에 추가합니다.
            const element = document.getElementById('iframe');
            const inHtml = `<iframe width="560" height="315" src="https://www.youtube.com/embed/` + data2 + "\"" + `frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>`;
            element.innerHTML = inHtml
            console.log(data2);
        }, error: function (error) {
            console.error('에러 발생: ', error);
        }
    });

});

//콘텐츠 상세 화면 출력
function recomCntntDetail(cntntId) {
    const formHtml = `
                    <form id="contentDetail" action="/cs/content/detail" method="get">
                        <input  id="targetContentIdF" name="targetContentIdF"  />
                    </form>`;

    const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    const form = doc.body.firstChild;
    document.body.append(form);
    document.getElementById("targetContentIdF").value = cntntId;
    document.getElementById('contentDetail').submit();
}

// 콘텐츠 수정 화면 요청
function updateCntnt(cntntId) {
    cntntId = document.getElementById('cntntId').value;
    // console.log(cntntId);
    const formHtml = `
                    <form id="updateCntnt" action="/cs/content/modify-form" method="get">
                        <input  id="targetContentIdF" name="targetContentIdF"  />
                    </form>`;

    const doc = new DOMParser().parseFromString(formHtml, 'text/html');
    const form = doc.body.firstChild;
    document.body.append(form);
    document.getElementById("targetContentIdF").value = cntntId;
    document.getElementById('updateCntnt').submit();
}

//콘텐츠 삭제 처리
function deleteCntnt() {
    if (confirm('컨텐츠를 삭제하시겠습니까?')) {
        $.ajax({
            url: '/cs/content', type: 'patch',
            data: {
                cntntId: document.getElementById("cntntId").value
            }, // 데이터 객체 전달
            success: function (data2) {
                if (confirm('삭제가 완료 되었습니다')) {
                    window.location.replace("/cs/content-manage");
                } else {
                    window.location.replace("/cs/content-manage");
                }
            }, error: function (error) {
                console.error('에러 발생: ', error);
            }
        });
    }

}

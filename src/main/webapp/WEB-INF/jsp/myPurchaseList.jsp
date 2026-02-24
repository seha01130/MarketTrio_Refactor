<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
    <title>My Purchase List</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        // 후기 작성 폼을 띄우는 함수
        /*function openReviewForm(SHPostId) {
            $.get("/myPurchaseList/giveRate/" + SHPostId, function(data) {
                var reviewFormHtml = `
                    <div id="reviewForm">
                        <input type="hidden" id="SHPostId" value="${data.SHPostId}">
                        <input type="hidden" id="senderId" value="${data.senderId}">
                        <input type="hidden" id="receiverId" value="${data.receiverId}">
                        <label for="rating">Rating:</label>
                        <input type="number" id="rating" name="rating" min="1" max="5" required>
                        <label for="comment">Comment:</label>
                        <textarea id="comment" name="comment" required></textarea>
                        <button onclick="submitReview(${data.SHPostId})">Submit</button>
                        <button onclick="closeReviewForm()">Cancel</button>
                    </div>`;
                $("#reviewFormContainer").html(reviewFormHtml);
                $("#reviewFormContainer").show();
            });
        }*/
        function openReviewForm(SHPostId) {
            $.get("/myPurchaseList/giveRate/" + SHPostId, function(data) {
                $("#reviewFormContainer").html(data);
                $("#reviewFormContainer").show();
            });
        }

        // 후기 작성 폼 제출 함수
        function submitReview(SHPostId) {
            var review = {
                SHPostId: SHPostId,
                senderId: $("#senderId").val(),
                receiverId: $("#receiverId").val(),
                rating: $("#rating").val(),
                comment: $("#comment").val()
            };

            $.ajax({
                url: "/myPurchaseList/giveRate/" + SHPostId,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(review),
                success: function(response) {
                    alert(response);
                    $("#reviewFormContainer").hide();
                    // 버튼 텍스트를 '후기 작성 완료'로 변경
                    $("#reviewButton_" + SHPostId).text("후기 작성 완료").prop("disabled", true);
                },
                error: function(xhr, status, error) {
                    alert("후기 작성에 실패했습니다. 다시 시도해주세요.");
                }
            });
        }
        
     	// 후기 작성 폼을 닫는 함수
        function closeReviewForm() {
            $("#reviewFormContainer").hide();
        }
    </script>
</head>
<body>
    <!-- 기존 코드 ... -->

     <div id="reviewFormContainer" style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%); background:white; padding:20px; box-shadow:0px 0px 10px rgba(0,0,0,0.5);">
        <!-- 리뷰 폼이 동적으로 삽입됩니다 -->
    </div>

    <table>
        <thead>
            <!-- Table Headers -->
        </thead>
        <tbody>
            <c:forEach var="item" items="${SHPurchaseList}">
                <tr>
                    <!-- 기존 데이터 셀들 -->
                    <td>
                        <button id="reviewButton_${item.SHPostId}" onclick="openReviewForm(${item.SHPostId})">
                            후기 작성하기
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- 후기 작성 폼 템플릿 -->
    <script type="text/template" id="reviewFormTemplate">
        <div>
            <input type="hidden" id="SHPostId" value="<%= SHPostId %>">
            <input type="hidden" id="senderId" value="<%= senderId %>">
            <input type="hidden" id="receiverId" value="<%= receiverId %>">
            <label for="rating">Rating:</label>
            <input type="number" id="rating" name="rating" min="1" max="5" required>
            <label for="comment">Comment:</label>
            <textarea id="comment" name="comment" required></textarea>
            <button onclick="submitReview(<%= SHPostId %>)">Submit</button>
        </div>
    </script>
</body>
</html>

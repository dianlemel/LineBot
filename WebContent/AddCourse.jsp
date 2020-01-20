<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-control" content="no-cache">
    <meta http-equiv="Cache" content="no-cache">
    <title>加入課程頁面</title>
    <script src="https://static.line-scdn.net/liff/edge/2.1/sdk.js"></script>
    <script>
        window.onload = function () {
            liff.init({
                liffId: '<%=line.sevlet.LineBot.LIFF_ID%>'
            }).then(() => {
                if (liff.isInClient()) {
                    liff.scanCode().then(result => {
                        if(result.value != 'null'){
                            liff.getProfile().then(function (profile) {
                                var xmlhttp = new XMLHttpRequest();
                                xmlhttp.open("POST","/AddCourse",true);
                                xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
                                xmlhttp.onreadystatechange = function(){
                                    if (xmlhttp.readyState==4 && xmlhttp.status==200){
                                        liff.closeWindow();
                                    }
                                }
                                xmlhttp.send("ID="+profile.userId+"&INVITE_CODE="+result.value);
                            }).catch(function (error) {
                                alert("獲得個人資訊錯誤");
                                liff.closeWindow();
                            });
                        }else{
                            liff.closeWindow();
                        }
                    }).catch(err => {
                        alert("QR Code掃描出錯");
                        liff.closeWindow();
                    });
                } else {
                    alert("請勿使用外部瀏覽器開啟");
                }
            }).catch((err) => {
                alert("請勿使用外部瀏覽器開啟");
            });
        };
    </script>
</head>
<body>
    <h1>
        請關閉該頁面
    </h1>
</body>
</html>
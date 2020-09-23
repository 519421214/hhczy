const dateFormat = 'yyyy-MM-dd hh:mm:ss';
let name;
Date.prototype.format = function (fmt) {
    let o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "s": this.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (let k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
};
let ws;
const msg_list = document.getElementById('msg_list');

function addMsg(msg) {
    msg_list.innerHTML += msg;
    msg_list.scrollTop = msg_list.scrollHeight;
}

function cleanup() {
    msg_list.innerHTML = '';
}

function send() {
    if (ws) {
        let input_msg = document.getElementById('input_msg');
        let text = input_msg.value;
        if (text) {
            ws.send(text);
            input_msg.value = '';
            addMsg(`<p class="text-info">客户端：<span>${new Date().format(dateFormat)}</span></p><p>${text}</p>`)
        }
    }
}

var nodeNo = 1;
function play(path) {
    //最多同时播放三个声音
    var player1 = $("#myAudio1")[0];
    var player2 = $("#myAudio2")[0];
    //反复覆盖播放
    if (nodeNo == 1) {
        nodeNo = 2;
        player1.setAttribute("src", path);
        player1.play();
    }else if(nodeNo = 2){
        nodeNo = 1;
        player2.setAttribute("src", path);
        player2.play();
    }
}

function connect(btn) {
    if (!"WebSocket" in window) {
        alert("您的浏览器不支持 WebSocket!");
    }
    let server = document.getElementById('input_server').value;
    if (!server) {
        return
    }
    name = server.substr(server.lastIndexOf("/") + 1);
    try {
        ws = new WebSocket(server);
    } catch (e) {
        addMsg(`<p class="text-error">${e}</p>`);
        return;
    }
    ws.onopen = function () {
        btn.classList.add('primary');
        addMsg(`<p class="text-success">连接成功，可发送消息到服务端</p>`)
    };
    ws.onmessage = function (e) {

        var data = e.data;

        if (/^\[audio\].*$/.test(data)) {
            var path = data.replace("[audio]", "");
            play(path);
        } else if (/^\[system\].*$/.test(data)) {
            var sysMsg = data.replace("[system]", "【" + name + "】");
            addMsg(`<p class="text-success">系统提示:<span>${new Date().format(dateFormat)}</span></p><p>${sysMsg}</p>`)
        } else {
            addMsg(`<p class="text-success">${name}:<span>${new Date().format(dateFormat)}</span></p><p>${data}</p>`)
        }
    };
    ws.onclose = function () {
        ws = null;
        btn.classList.remove('primary');
        addMsg(`<p class="text-error">连接已关闭</p>`)
    };
    ws.onerror = function (e) {
        btn.classList.remove('primary');
        addMsg(`<p class="text-error">${e}</p>`)
    }
}

function disconnect() {
    if (ws) {
        ws.close();
        ws = null
    }
}

document.getElementById('input_msg').addEventListener('keydown', function (event) {
    if (event.keyCode === 13) {
        send()
    }
});
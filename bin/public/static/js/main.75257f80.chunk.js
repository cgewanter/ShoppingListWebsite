(window.webpackJsonp=window.webpackJsonp||[]).push([[0],{15:function(t,e,n){},18:function(t,e,n){"use strict";n.r(e);var a=n(0),o=n.n(a),c=n(8),r=n.n(c),i=(n(15),n(1)),s=n(2),l=n(4),u=n(3),d=n(5),h=(n(6),function(t){function e(t){var n;return Object(i.a)(this,e),(n=Object(l.a)(this,Object(u.a)(e).call(this,t))).addCard=function(){console.log("adding card!"),console.log(n.state),fetch("/cards",{method:"POST",body:JSON.stringify(n.state),headers:{"Content-Type":"application/json"}})},n.setText=function(t){console.log(t),n.setState({text:t.target.value})},n.setColor=function(t){console.log(t),n.setState({color:t.target.value})},n.state={text:"",color:""},n}return Object(d.a)(e,t),Object(s.a)(e,[{key:"render",value:function(){return o.a.createElement(o.a.Fragment,null,o.a.createElement("input",{id:"text",value:this.state.text,onChange:this.setText}),o.a.createElement("input",{id:"color",value:this.state.color,onChange:this.setColor}),o.a.createElement("button",{onClick:this.addCard},"Add"))}}]),e}(a.Component)),f=function(t){function e(t){var n;return Object(i.a)(this,e),(n=Object(l.a)(this,Object(u.a)(e).call(this,t))).setNewCards=function(t){n.setState({cards:t})},n.state={cards:[]},n}return Object(d.a)(e,t),Object(s.a)(e,[{key:"componentDidMount",value:function(){console.log("here is page 2");var t=this.setNewCards;fetch("/cards").then(function(e){e.json().then(function(e){t(e),e.forEach(function(t){return console.log(t)})})})}},{key:"render",value:function(){return console.log("rending",this.state),o.a.createElement("div",{className:"App"},this.state.cards.map(function(t){return o.a.createElement("div",{style:{color:t.color}},t.text)}))}}]),e}(a.Component),m=function(t){function e(t){var n;return Object(i.a)(this,e),(n=Object(l.a)(this,Object(u.a)(e).call(this,t))).state={currentPage:1},n}return Object(d.a)(e,t),Object(s.a)(e,[{key:"render",value:function(){var t=this;return o.a.createElement(o.a.Fragment,null,o.a.createElement("div",{className:"App"},1===this.state.currentPage?o.a.createElement(h,null):o.a.createElement(f,null)),o.a.createElement("button",{onClick:function(){t.setState({currentPage:1})}},"Button1"),o.a.createElement("button",{onClick:function(){t.setState({currentPage:2})}},"Button2"))}}]),e}(a.Component);Boolean("localhost"===window.location.hostname||"[::1]"===window.location.hostname||window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));r.a.render(o.a.createElement(m,null),document.getElementById("root")),"serviceWorker"in navigator&&navigator.serviceWorker.ready.then(function(t){t.unregister()})},6:function(t,e,n){},9:function(t,e,n){t.exports=n(18)}},[[9,2,1]]]);
//# sourceMappingURL=main.75257f80.chunk.js.map
"use strict";(self.webpackChunkelectro_rent=self.webpackChunkelectro_rent||[]).push([[447],{724:(n,e,t)=>{t.d(e,{Z:()=>l});var i=t(4420),o=t(7689),r=t(8089),s=t(184);const l=()=>{const n=(0,o.s0)(),e=(0,i.I0)();return{loginState:(0,i.v9)((n=>n.loginSlice)),isLogin:"true"===sessionStorage.getItem("isLogin"),doLogin:async n=>{let{loginParam:t,successFn:i,failFn:o,errorFn:s}=n;return(await e((0,r.ft)({loginParam:t,successFn:i,failFn:o,errorFn:s}))).payload},doLogout:()=>{e((0,r.kS)())},moveToPath:e=>{n({pathname:e},{replace:!0})},moveToLogin:()=>(0,s.jsx)(o.Fg,{replace:!0,to:"/member/login"})}}},1447:(n,e,t)=>{t.d(e,{Z:()=>X});var i,o,r,s,l=t(2791),d=t(8323),a=t(2378),c=t(7689),x=t(1087),p=t(4420),h=(t(8089),t(724)),g=t(168),u=t(225);const m=u.Z.div(i||(i=(0,g.Z)(["\n  position: fixed;\n  display: block;\n  z-index: 100;\n  top: 155px;\n  width: 540px;\n  height: 310px;\n  border: 1px solid #2c39b5;\n  background: #fff;\n  padding: 30px 20px;\n"]))),f=u.Z.div(o||(o=(0,g.Z)(["\n  display: flex;\n  margin-bottom: 20px;\n  color: #4b4b4b;\n  font-size: 14px;\n  font-style: normal;\n  font-weight: 400;\n  line-height: 16px;\n  text-align: center;\n"]))),b=u.Z.div(r||(r=(0,g.Z)(["\n  width: 130px;\n  height: 34px;\n  background: #f2f2ff;\n  padding-top: 9px;\n"]))),v=u.Z.div(s||(s=(0,g.Z)(["\n  display: flex;\n  margin-left: 20px;\n  margin-top: 9px;\n  gap: 20px;\n  li {\n    color: #777;\n    font-size: 12px;\n    font-style: normal;\n    font-weight: 400;\n    line-height: 16px;\n    cursor: pointer;\n  }\n"])));var j=t(184);const w=[{id:1,title:"\uc2a4\ub9c8\ud2b8 \uae30\uae30"},{id:2,title:"PC / \ub178\ud2b8\ubd81"},{id:3,title:"\uc601\uc0c1 / \uce74\uba54\ub77c"},{id:4,title:"\uc74c\ud5a5"},{id:5,title:"\uac8c\uc784 \uae30\uae30"}],Z=[[{id:1,title:"\uc2a4\ub9c8\ud2b8 \uc6cc\uce58"},{id:2,title:"\ud0dc\ube14\ub9bf"},{id:3,title:"\uac24\ub7ed\uc2dc"},{id:4,title:"\uc544\uc774\ud3f0"}],[{id:1,title:"\ub178\ud2b8\ubd81"},{id:2,title:"PC"},{id:3,title:"\ub9c8\uc6b0\uc2a4"},{id:4,title:"\ud0a4\ubcf4\ub4dc"}],[{id:1,title:"\ube54\ud504\ub85c\uc81d\ud130"},{id:2,title:"\uc14b\ud1b1\ubc15\uc2a4"},{id:3,title:"\uce74\uba54\ub77c"},{id:4,title:"\ucea0\ucf54\ub354"},{id:5,title:"DSLR"}],[{id:1,title:"\uc2a4\ud53c\ucee4"},{id:2,title:"\uc774\uc5b4\ud3f0"},{id:3,title:"\ud5e4\ub4dc\ud3f0"},{id:4,title:"\ub9c8\uc774\ud06c"}],[{id:1,title:"\ud50c\ub808\uc774\uc2a4\ud14c\uc774\uc158"},{id:2,title:"\ub2cc\ud150\ub3c4"},{id:3,title:"Wii"},{id:4,title:"XBOX"},{id:5,title:"\uae30\ud0c0"}]],y=()=>{const[n,e]=(0,l.useState)(null),t=()=>{e(null)},i=(0,c.s0)();return(0,j.jsx)(m,{children:w.map((o=>(0,j.jsxs)(f,{children:[(0,j.jsx)(b,{children:o.title}),(0,j.jsx)(v,{children:Z[o.id-1].map((r=>(0,j.jsx)("li",{title:r.title,onClick:()=>{i("/more?mc=".concat(o.id,"&sc=").concat(r.id,"&page=1"),{state:{title:r.title}}),window.location.reload()},onMouseEnter:()=>{return n=r.title,void e(n);var n},onMouseLeave:t,style:n===r.title?{color:"#2C39B5",fontWeight:"500"}:{},children:r.title},r.id)))})]},o.id)))})};var k=t(5294),S=t(3014);const C=n=>{let{searchName:e,pageNum:t}=n;const[i,o]=(0,l.useState)(!0),[r,s]=(0,l.useState)(!1),[g,u]=(0,l.useState)(!1),[m,f]=(0,l.useState)(""),[b,v]=(0,l.useState)(""),[C,E]=(0,l.useState)(""),z=(0,l.useRef)(null),F=(0,l.useRef)(null),L=n=>{n.preventDefault(),sessionStorage.setItem("searchValue",m);(async n=>{let{sendData:e,successFn:t,failFn:i,errFn:o}=n;try{const n="".concat(S.L,"/api/prod?search=").concat(e.search,"&page=").concat(e.pageNum,"&mc=").concat(e.mc,"&sc=").concat(e.sc),o=await k.Z.get(n);"2"===o.status.toString().charAt(0)?t(o.data):i(o.data)}catch(r){o(r)}})({sendData:{search:m,mc:b,sc:C,pageNum:1},successFn:I,failFn:M,errFn:D})},I=n=>{console.log("\uac80\uc0c9 \uc131\uacf5",n);const e=sessionStorage.getItem("searchValue");let t="/search?search=".concat(m);b&&(t+="&mc=".concat(b)),C&&(t+="&sc=".concat(C)),X(t,{state:{result:n,searchValue:e}}),window.location.reload()},M=n=>{console.log("\uac80\uc0c9 \uc624\ub958",n)},D=n=>{console.log("\uac80\uc0c9 \uc11c\ubc84\uc5d0\ub7ec",n)},[N,B]=(0,l.useState)(""),[O,P]=(0,l.useState)([{id:0,title:"\uc804\uccb4"}]),[R,T]=(0,l.useState)(""),V=n=>{r&&null===n.target.closest(".header-search")&&s(!1)},A=n=>{g&&null===n.target.closest(".header-search")&&u(!1)},X=(0,c.s0)(),[_,K]=(0,l.useState)(0),U=()=>{K(window.scrollY||document.documentElement.scrollTop)};(0,l.useEffect)((()=>(document.addEventListener("click",A),()=>{document.removeEventListener("click",A)})),[g]),(0,l.useEffect)((()=>(document.addEventListener("click",V),()=>{document.removeEventListener("click",V)})),[r]),(0,l.useEffect)((()=>(window.addEventListener("scroll",U),()=>{window.removeEventListener("scroll",U)})),[]);const[W,q]=(0,l.useState)(!1),Q=(0,l.useRef)(),Y=()=>{q((n=>!n))};(0,l.useEffect)((()=>{const n=n=>{W&&!Q.current.contains(n.target)&&q(!1)};return document.addEventListener("mousedown",n),()=>{document.removeEventListener("mousedown",n)}}),[W]);const[H,G]=(0,l.useState)(null),[J,$]=(0,l.useState)(null),nn=()=>{$(null)},{moveToPath:en,isLogin:tn,doLogout:on}=((0,p.v9)((n=>n.loginSlice)),(0,h.Z)());(0,p.I0)();return(0,j.jsxs)(d.I5,{style:_<30?{boxShadow:"none"}:{boxShadow:"0px 2px 5px 0px rgba(0, 0, 0, 0.25)"},children:[(0,j.jsxs)(d.nx,{children:[(0,j.jsx)(d.OE,{src:"/images/logo.svg",onClick:()=>{X("/")}}),(0,j.jsx)("div",{className:"header-search",children:(0,j.jsxs)(d.UI,{children:[(0,j.jsx)(d.EQ,{ref:z,onChange:n=>(n=>{f(n.target.value)})(n),onKeyDown:n=>{"Enter"===n.key&&(n.preventDefault(),F.current.click())},type:"text",placeholder:"\uac80\uc0c9\uc5b4\ub97c \uc785\ub825\ud574\uc8fc\uc138\uc694.",min:2,value:m,onMouseEnter:()=>o(!1),onMouseLeave:()=>o(!0),onClick:()=>{u(!g),s(!1)},style:{backgroundColor:g?"#FFF":"",borderRadius:g?"45px":"",boxShadow:g?"4px 0px 8px 0px rgba(0, 0, 0, 0.25)":""}}),i&&!g&&(0,j.jsx)(d.oq,{}),(0,j.jsxs)(d.vI,{onMouseEnter:()=>o(!1),onMouseLeave:()=>o(!0),onClick:()=>{s(!r),u(!1)},style:{backgroundColor:r?"#FFF":"",borderRadius:r?"45px":"",boxShadow:r?"-4px 0px 8px 0px rgba(0, 0, 0, 0.25)":""},children:[(0,j.jsx)("b",{children:"\uce74\ud14c\uace0\ub9ac"}),(0,j.jsxs)("div",{children:[(0,j.jsx)("span",{children:N||"\uba54\uc778 \uce74\ud14c\uace0\ub9ac"}),(0,j.jsx)("p",{}),(0,j.jsx)("span",{children:R||"\uc0c1\uc138 \uce74\ud14c\uace0\ub9ac"})]})]}),r&&(0,j.jsxs)(d.oX,{children:[(0,j.jsxs)("div",{children:[(0,j.jsx)("h1",{children:"\uba54\uc778 \uce74\ud14c\uace0\ub9ac"}),(0,j.jsxs)("select",{onChange:n=>{const e=w.find((e=>e.id===parseInt(n.target.value)));B(e?e.title:""),v(e?e.id:"");const t=parseInt(n.target.value);if(w.find((n=>n.id===t))){const n=Z[t-1];P(n)}},defaultValue:"",children:[(0,j.jsx)("option",{value:"",disabled:!0,hidden:!0,children:"\uc804\uccb4"}),w.map((n=>(0,j.jsx)("option",{value:n.id,children:n.title},n.id)))]})]}),(0,j.jsxs)("div",{children:[(0,j.jsx)("h1",{children:"\uc0c1\uc138 \uce74\ud14c\uace0\ub9ac"}),(0,j.jsxs)("select",{onChange:n=>{const e=O.find((e=>e.title===n.target.value));T(e?e.title:""),E(e?e.id:"")},defaultValue:"",children:[(0,j.jsx)("option",{value:"",disabled:!0,hidden:!0,children:"\uc804\uccb4"}),O.map((n=>(0,j.jsx)("option",{children:n.title},n.id)))]})]})]}),(0,j.jsx)(d.VP,{ref:F,onClick:n=>L(n),type:"button"})]})}),tn?(0,j.jsxs)(d.zD,{children:[(0,j.jsx)("button",{onClick:()=>{on(),en("/")},children:"\ub85c\uadf8\uc544\uc6c3"}),(0,j.jsx)(a.O3,{}),(0,j.jsx)(x.rU,{to:"/my",onClick:()=>{var n;n="\ub300\uc5ec\uc911",sessionStorage.setItem("selectedItem",n)},children:(0,j.jsx)("button",{children:"\ub9c8\uc774\ud398\uc774\uc9c0"})})]}):(0,j.jsxs)(d.zD,{children:[(0,j.jsx)("button",{onClick:()=>{X("/login")},children:"\ub85c\uadf8\uc778"}),(0,j.jsx)(a.O3,{}),(0,j.jsx)("button",{onClick:()=>{X("/join/step_1")},children:"\ud68c\uc6d0\uac00\uc785"})]})]}),(0,j.jsxs)(d.y7,{children:[(0,j.jsxs)(d.gA,{ref:Q,children:[!0===W?(0,j.jsx)("img",{src:"/images/header/bt_cancel.svg",onClick:Y}):(0,j.jsx)("img",{src:"/images/header/bt_menu.svg",onClick:Y}),W&&(0,j.jsx)(y,{})]}),(0,j.jsxs)(d.C3,{children:[w.map((n=>(0,j.jsxs)(d.st,{onMouseEnter:()=>{return e=n.title,void G(e);var e},onMouseLeave:()=>(n.title,void G(null)),className:H===n.title?"active":"",children:[(0,j.jsx)(d.BM,{style:H===n.title?{color:"#2C39B5",fontWeight:"500",fontSize:"13px"}:{color:"#777"},children:n.title}),H===n.title&&(0,j.jsx)(d.z1,{children:Z[n.id-1].map((e=>(0,j.jsx)("li",{title:e.title,onClick:()=>{X("/more?mc=".concat(n.id,"&sc=").concat(e.id,"&page=1"),{state:{title:e.title}}),window.location.reload()},onMouseEnter:()=>{return n=e.title,void $(n);var n},onMouseLeave:nn,style:J===e.title?{color:"#2C39B5",fontWeight:"500",background:"#F2F2FF"}:{},children:e.title},e.id)))})]},n.title))),(0,j.jsx)("div",{})]})]})]})};var E,z,F,L,I,M,D;const N=u.Z.div(E||(E=(0,g.Z)(["\n  width: 100vw;\n  text-align: center;\n  margin: 0 auto;\n  border-top: 1px solid #f2f2ff;\n"]))),B=u.Z.div(z||(z=(0,g.Z)(["\ntext-align: start;\n  width: 1300px;\n  margin: 0 auto;\n  height: 70px;\n  padding-top: 30px;\n"]))),O=u.Z.img(F||(F=(0,g.Z)(["\n  width: 60px;\n  height: 50px;\n  background-color: transparent;\n"]))),P=u.Z.div(L||(L=(0,g.Z)(["\n  width: 1300px;\n  margin: 0 auto;\n  display: flex;\n  height: 270px;\n\n  padding-top: 25px;\n  div {\n    text-align: start;\n  }\n  h1 {\n    text-align: start;\n    color: #777;\n    font-size: 14px;\n    font-style: normal;\n    font-weight: 400;\n    line-height: normal;\n  }\n  h2 {\n    color: #2c39b5;\n    font-size: 14px;\n    font-style: normal;\n    font-weight: 700;\n    line-height: normal;\n  }\n  img {\n    margin-right: 15px;\n  }\n"]))),R=u.Z.div(I||(I=(0,g.Z)(["\n  width: 550px;\n"]))),T=u.Z.div(M||(M=(0,g.Z)(["\n  width: 610px;\n"]))),V=u.Z.div(D||(D=(0,g.Z)(["\n  border-top: 1px solid #2c39b5;\n  height: 60px;\n  span {\n    color: #777;\n    font-size: 14px;\n    font-style: normal;\n    font-weight: 400;\n    line-height: normal;\n  }\n"]))),A=()=>(0,j.jsxs)(N,{children:[(0,j.jsx)(B,{children:(0,j.jsx)(O,{src:"/images/logo.svg"})}),(0,j.jsxs)(P,{children:[(0,j.jsxs)(R,{children:[(0,j.jsx)("h1",{children:"\uc0c1\ud638:Billy"}),(0,j.jsx)("h1",{children:"\ud300\uc7a5:\ubc15\uc900\uc11c,\ud55c\uc0c1\uc6d0"}),(0,j.jsx)("br",{}),(0,j.jsx)("h1",{children:"\uae43 \uc8fc\uc18c:"}),(0,j.jsx)("h1",{children:"https://github.com/\uacc4\uc815\uba85/\ud504\ub85c\uc81d\ud2b8\uba85"}),(0,j.jsx)("br",{}),(0,j.jsx)("br",{}),(0,j.jsx)("h1",{children:"\uc804\ud654\ubc88\ud638 : 0507-1414-1018"}),(0,j.jsx)("h1",{children:"\ud300\uc7a5 \uc774\uba54\uc77c : 0000@gmail.com & 0000@gmail.com"}),(0,j.jsx)("br",{}),(0,j.jsx)("br",{}),(0,j.jsx)("h1",{children:"\uc8fc\uc18c : \ub300\uad6c \uc911\uad6c \uc911\uc559\ub300\ub85c 394 \uc81c\uc77c\ube4c\ub529 5F "})]}),(0,j.jsxs)(T,{children:[(0,j.jsx)("h2",{children:"COMPANY"}),(0,j.jsx)("br",{}),(0,j.jsx)("br",{}),(0,j.jsx)("h1",{children:"\ud68c\uc0ac\uc18c\uac1c \uc774\uc6a9\uc57d\uad00 \uac1c\uc778\uc815\ubcf4\ucc98\ub9ac\ubc29\uce68 \uc81c\ud734\ubb38\uc758 \uace0\uac1d\uc13c\ud130"}),(0,j.jsx)("br",{}),(0,j.jsx)("br",{}),(0,j.jsx)("h2",{children:"INTRODUCE"}),(0,j.jsx)("br",{}),(0,j.jsx)("br",{}),(0,j.jsx)("h1",{children:"FRONT-END \ubc15\uc900\uc11c \uae40\uacbd\ubbfc \ubc15\uc18c\uc5f0 \uc0ac\uacf5\uc740\uc9c4 \ucd5c\ubc30\uadfc"}),(0,j.jsx)("br",{}),(0,j.jsx)("br",{}),(0,j.jsx)("h1",{children:"BACK-END \ud55c\uc0c1\uc6d0 \uae40\ud604\ube48 \uae40\ud604\uc77c \uc870\ud604\ubbfc"})]}),(0,j.jsxs)("div",{children:[(0,j.jsx)("h2",{children:"SOCIAL"}),(0,j.jsx)("br",{}),(0,j.jsx)("br",{}),(0,j.jsx)("img",{src:"/images/footer/gitHub.svg"}),(0,j.jsx)("img",{src:"/images/footer/notion.svg"}),(0,j.jsx)("img",{src:"/images/footer/figma.svg"})]})]}),(0,j.jsx)(V,{children:(0,j.jsx)("span",{children:"Copyright \xa9 \ud68c\uc0ac\uba85 Inc. all rights reserved. "})})]}),X=n=>{let{children:e}=n;return(0,j.jsxs)("div",{children:[(0,j.jsx)("header",{children:(0,j.jsx)(C,{})}),(0,j.jsxs)("div",{style:{position:"absolute",top:"250px",left:"0",width:"100%"},children:[(0,j.jsx)("main",{children:e}),(0,j.jsx)("footer",{children:(0,j.jsx)(A,{})})]})]})}},8323:(n,e,t)=>{t.d(e,{BM:()=>N,C3:()=>M,EQ:()=>C,I5:()=>w,OE:()=>y,UI:()=>k,VP:()=>z,gA:()=>I,nx:()=>Z,oX:()=>O,oq:()=>E,st:()=>D,vI:()=>S,y7:()=>L,z1:()=>B,zD:()=>F});var i,o,r,s,l,d,a,c,x,p,h,g,u,m,f,b,v=t(168),j=t(225);const w=j.Z.div(i||(i=(0,v.Z)(["\n  position: fixed;\n  z-index: 100;\n  top: 0;\n  left: 50%;\n  transform: translateX(-50%);\n  background: #fff;\n  width: 100vw;\n  text-align: center;\n  padding-bottom: 15px;\n  height: 155px;\n"]))),Z=j.Z.div(o||(o=(0,v.Z)(["\n  margin: 30px auto;\n  display: flex;\n  justify-content: space-between;\n  align-items: center;\n  width: 1300px;\n"]))),y=j.Z.img(r||(r=(0,v.Z)(["\n  width: 90px;\n  height: 60px;\n  background-color: transparent;\n  cursor: pointer;\n"]))),k=j.Z.div(s||(s=(0,v.Z)(["\n  display: flex;\n  position: relative;\n  width: 640px;\n  height: 60px;\n  align-items: center;\n  border-radius: 80px;\n  border: 1px solid #2c39b5;\n  input {\n    width: 350px;\n    height: 58px;\n    padding-left: 20px;\n  }\n  input:hover {\n    width: 350px;\n    background-color: #eee;\n    border-radius: 45px;\n  }\n  select {\n    width: 120px;\n    height: 22px;\n    font-size: 12px;\n    border: 1px solid #2c39b5;\n  }\n"]))),S=j.Z.div(l||(l=(0,v.Z)(['\n    width: 290px;\n    height: 58px;\n    :hover {\n      background-color: #eee;\n      border-radius: 45px;\n    }\n    b {\n    display: flex;\n    height: 50%;\n    font-size: 16px;\n    color: #777;\n    font-weight: 400;\n    padding-top: 10px;\n    padding-left: 25px;\n    }\n    >div {\n      display: flex;\n      align-items: center;\n      gap: 5px;\n      height: 50%;\n      padding-left: 25px;\n      p {\n        display: block;\n        content: "";\n        width: 1px;\n        height: 10px;\n        background: #2c39b5;\n      }\n      span {\n        line-height: 1.6;\n        font-size: 12px;\n      }\n    }\n']))),C=j.Z.input(d||(d=(0,v.Z)(["\n  display: flex;\n  width: 350px;\n  height: 60px;\n  flex-direction: column;\n  justify-content: center;\n  flex-shrink: 0;\n  border: none;\n  background-color: transparent;\n  color: rgb(0, 0, 0);\n  font-size: 16px;\n  font-style: normal;\n  line-height: 24px; /* 80% */\n  letter-spacing: 0.5px;\n"]))),E=j.Z.div(a||(a=(0,v.Z)(["\nwidth: 1px;\nheight: 26px;\nbackground: #eee;\n"]))),z=j.Z.button(c||(c=(0,v.Z)(['\n  position: absolute;\n  right: 10px;\n  width: 40px;\n  height: 40px;\n  border-radius: 100%;\n  flex-shrink: 0;\n  border: none;\n  background-image: url("/images/header/bt_search.svg");\n  cursor: pointer;\n']))),F=j.Z.div(x||(x=(0,v.Z)(["\n  display: flex;\n  align-items: center;\n  width: 200px;\n  height: 20px;\n  justify-content: space-between;\n  button {\n    width: 80px;\n    background: transparent;\n    border: none;\n    color: #777;\n    font-size: 14px;\n    font-style: normal;\n    font-weight: 400;\n    line-height: 16px;\n    cursor: pointer;\n  }\n"]))),L=j.Z.div(p||(p=(0,v.Z)(["\n  margin: 0 auto;\n  width: 1300px;\n  display: flex;\n  gap: 40px;\n  ul {\n    gap: 40px;\n    display: flex;\n    list-style: none;\n  }\n  li {\n    color: #4b4b4b;\n    font-size: 12px;\n    font-style: normal;\n    font-weight: 400;\n    line-height: 16px;\n  }\n"]))),I=j.Z.div(h||(h=(0,v.Z)(["\n  display: block;\n  width: 35px;\n  height: 35px;\n  img {\n    padding: 7px;\n    cursor: pointer;\n  }\n"]))),M=j.Z.div(g||(g=(0,v.Z)(["\n  display: flex;\n  gap: 5px;\n"]))),D=j.Z.div(u||(u=(0,v.Z)(["\n  width: 100px;\n"]))),N=j.Z.li(m||(m=(0,v.Z)(["\n  height: 30px;\n  width: 85px;\n  margin: 0 auto 5px;\n  padding-top: 6px;\n  cursor: pointer;\n"]))),B=j.Z.div(f||(f=(0,v.Z)(["\n  display: block;\n  border: 1px solid #2c39b5;\n  width: 100px;\n  padding-top: 10px;\n  background: #fff;\n  li {\n    margin-bottom: 10px;\n    padding: 7px 0;\n    color: #777;\n    cursor: pointer;\n  }\n"]))),O=j.Z.div(b||(b=(0,v.Z)(["\n  display: block;\n  position: absolute;\n  top: 70px;\n  right: 20px;\n  border: 1px solid #2c39b5;\n  width: 245px;\n  height: 90px; \n  padding: 15px;\n  background: #fff;\n  div {\n    display: flex;\n    gap: 20px;\n    margin-bottom: 20px;\n    h1 {\n      font-size: 12px;\n      font-weight: 400;\n      color: #777;\n    }\n  }\n"])))},2378:(n,e,t)=>{t.d(e,{EB:()=>b,Ko:()=>m,Nm:()=>v,O3:()=>j,TR:()=>u,Xn:()=>h,aB:()=>f,nw:()=>g});var i,o,r,s,l,d,a,c,x=t(168),p=t(225);const h=p.Z.div(i||(i=(0,x.Z)(["\n  width: 1300px;\n  /* text-align: center; */\n  margin: 0 auto;\n  /* background: skyblue; */\n  padding-bottom: 120px;\n"]))),g=p.Z.div(o||(o=(0,x.Z)(["\nwidth: 100%;\ntext-align: center;\n"]))),u=p.Z.img(r||(r=(0,x.Z)(["\n  margin: 0px auto 30px auto;\n  width: 80px;\n  height: 60px;\n  background-color: transparent;\n"]))),m=p.Z.div(s||(s=(0,x.Z)(["\n  margin: 0 auto;\n  /* box-sizing: border-box; */\n  width: 450px;\n  height: ",";\n  /* height: 540px; */\n  border-radius: 10px;\n  border: 1px solid #2c39b5;\n  padding-left: 30px;\n  p {\n    margin-top: 50px;\n    margin-bottom: ",";\n\n    color: #000;\n    font-family: KyivType Sans;\n    font-size: 14px;\n    font-style: normal;\n    font-weight: 350;\n    line-height: normal;\n  }\n  li {\n    position: relative;\n    cursor: pointer;\n\n    color: #000;\n    font-family: KyivType Sans;\n    font-size: 12px;\n    font-style: normal;\n    font-weight: 400;\n    line-height: normal;\n  }\n  \n"])),(n=>n.height?n.height:"540px"),(n=>n.mgbtm?n.mgbtm:"160px")),f=p.Z.input(l||(l=(0,x.Z)(["\n  width: 390px;\n  height: 50px;\n  border-radius: 10px;\n  border: 1px solid #2c39b5;\n  padding-left: 20px;\n\n  color: #000;\n  font-size: 14px;\n  font-style: normal;\n  font-weight: 400;\n  line-height: normal;\n\n  margin-bottom: 10px;\n"]))),b=(0,p.Z)(f)(d||(d=(0,x.Z)(["\n  margin-bottom: 30px;\n"]))),v=p.Z.button(a||(a=(0,x.Z)(["\n  width: 390px;\n  height: 50px;\n  border-radius: 10px;\n  background: #2c39b5;\n  margin-bottom: 20px;\n  border: 0;\n  color: #fff;\n  font-size: 14px;\n  font-style: normal;\n  font-weight: 400;\n  line-height: normal;\n  cursor: pointer;\n"]))),j=p.Z.div(c||(c=(0,x.Z)(["\n  width: 1px;\n  height: 15px;\n  background: ",";\n"])),(n=>n.color?n.color:"#2c39b5"))}}]);
//# sourceMappingURL=447.275cb693.chunk.js.map
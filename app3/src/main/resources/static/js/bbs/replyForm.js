'use strict';

//등록
writeBtn.addEventListener("click", e=>{
  writeForm.submit();
});

//목록
listBtn.addEventListener("click", e=>{
  location.href = "/bbs";
});

//작성자명 앞 4글자 표시 후 익명처리
const $NC = document.getElementById('names').textContent;
const $textChange = $NC.replace($NC.substring(4), "****");
names.textContent = $textChange;
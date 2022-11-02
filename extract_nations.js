let ipTable = document.getElementById("ip-address");
let tableBody = ipTable.children[1];
let output = [];
for(child of tableBody.children){
    output.push({ip:child.children[0].innerText,range:child.children[2].innerText});
}
console.log(JSON.stringify(output));
import Parser from "./Parser.mjs";
const parser=new Parser()
const program="43"
const ast=parser.parse(program)
console.log(JSON.stringify(ast))
import Tokenizer from "./Tokenizer.mjs"

class Parser{



    constructor(){
        this.tokenizer=new Tokenizer()
        this.string=""
    }
    parse(string){
        
        this.string=string

        this.tokenizer.init(string);

        this.lookahead=this.tokenizer.getNextToken()
        return this.Program()

    }


    //here we define our program ,
    //our program in this case is just a number so we return its ast object
    Program(){
        return {
            type:"Program",
            body:this.NumericLiteral()}
    }


    // we define the ast object for the numeric Literal
    NumericLiteral(){

        const token=this.eat("NUMBER")
        return {
            type:"NumericLiteral",
            value:Number(token.value)
        }
    }

    eat(tokenType){
        const token=this.lookahead

        if(token==null){
            throw new SyntaxError(
                `Unexpected end of input , expected ${tokenType}`
            )
        }

        if(token.type!=tokenType){
            throw new SyntaxError(
                `Unexpected end of input , expected ${tokenType} but got ${token.type}`
            )
        }
        this.lookahead=this.tokenizer.getNextToken()
        return token;
    }
}
export default Parser
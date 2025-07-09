class Tokenizer{
    init( string){
        this.string=string;
        this.cursor=0;
    }
    hasMoreTokens(){
        return this.cursor<this.string.length
    }
    getNextToken(){
        if(!this.hasMoreTokens()){
            return null;
        }

        const string=this.string.slice(this.cursor)

        if(!Number.isNaN(Number(string[0]))){
            let number=""
            while(!Number.isNaN(Number(string[this.cursor]))){
                number+=String(string[this.cursor])
                this.cursor+=1
            }

            return{
                type:"NUMBER",
                value:Number(number)
            
            }
        }


        //string 
    
    }

   
    
}

export default Tokenizer
<!-- Backend del tateti -->
function Tabla(){
	this.id = -1;
	this.p1="";
	this.p2="";
	this.turno=true;
	this.end = false;
	this.empate = true;
	this.count = 0;
	this.ganador = function(){
		return (this.turno?this.p1:this.p2);
	}
	this.simbol = function(x){
		if(x==true)return 'o';
		if(x==false)return 'X';
		return '&nbsp;&nbsp;';
	}
	this.jugar = function(a){
		
		if(this.mat[a]!==-1 || this.end){
			return false;
		}
		this.count = this.count + 1;
		this.mat[a]=this.turno;
		ret = this.termino();
		if(ret)console.log("Gano "+(this.turno?this.p1:this.p2));
		if(this.count === 9) this.end = true;
		if(!this.end)
			this.turno = !this.turno;
		return ret;
	}
	this.mat=[-1,-1,-1,-1,-1,-1,-1,-1,-1];
	this.init = function(){
		if(this.p1 === "")
			this.p1 = "Jugador 1";
		if(this.p2 === "")
			this.p2 = "Jugador 2";
		this.count = 0;
		this.empate = true;
		this.turno = true;
		this.mat=[-1,-1,-1,-1,-1,-1,-1,-1,-1];
		this.end = false;
	}
	this.p_ganadoras = [[0,1,2],[0,4,8],[0,3,6],[1,4,7],[2,5,8],[2,4,6],[3,4,5],[6,7,8]];
	this.termino = function(){
		
		for (var i = 0 ; i < 8; i++) {
			if(this.mat[this.p_ganadoras[i][0]]==this.turno && this.mat[this.p_ganadoras[i][1]]==this.turno && this.mat[this.p_ganadoras[i][2]]==this.turno){
				this.end = true;
				this.empate = false;
				return true;
			}
		}
		return false;
	}
}

var propType = {
	'class' : 0,
	'id':0,
	'style':0,
	'innerHTML' :1,
	'type':1
};

function _(element,data){
	var _tmp_ = document.createElement(element);
    var props = {};
    for(_$1=0;_$1<data.length;++_$1){
        var prop = data[_$1].split("=");
        props[prop[0]] = prop[1];
    }
    for(let tp in props){
       		if(propType[tp]==1) _tmp_[tp] = props[tp];
    		else _tmp_.setAttribute(tp,props[tp]);
	}
	return _tmp_;
}

class ele{
    constructor(element,data){
        this.cx = true;
        this.child = [];
        this.object = _(element,data);
    }

    add(x){
        this.child.push(x);
        return this;
    }

    get(){
        while(this.child.length>0){
            var curr = this.child.shift();
            if(curr.cx == undefined){
                this.object.appendChild(curr);
            } else {
                this.object.appendChild(curr.get());
            }
        }
        return this.object;
    }
}

function $(element,data){
    return new ele(element,data);
}
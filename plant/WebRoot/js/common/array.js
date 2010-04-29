Array.prototype.addAll = function ($array) {
	if ($array == null || $array.length == 0) {
		return;
	}
	for (var $i = 0; $i < $array.length; $i++) {
		this.push($array[$i]);
	}
};
Array.prototype.contains = function ($value) {
	for (var $i = 0; $i < this.length; $i++) {
		var $element = this[$i];
		if ($element == $value) {
			return true;
		}
	}
	return false;
};
Array.prototype.indexOf = function ($value) {
	for (var $i = 0; $i < this.length; $i++) {
		if (this[$i] == $value) {
			return $i;
		}
	}
	return -1;
};
Array.prototype.insertAt = function ($value, $index) {
	if ($index < 0) {
		this.unshift($value);
	} else {
		if ($index >= this.length) {
			this.push($value);
		} else {
			this.splice($index, 0, $value);
		}
	}
};
Array.prototype.remove = function ($value) {
	var $index = this.indexOf($value);
	if ($index != -1) {
		this.splice($index, 1);
	}
};
Array.prototype.removeAll = function () {
	while (this.length > 0) {
		this.pop();
	}
};
Array.prototype.replace = function ($oldValue, $newValue) {
	for (var $i = 0; $i < this.length; $i++) {
		if (this[$i] == $oldValue) {
			this[$i] = $newValue;
			return;
		}
	}
};
Array.prototype.swap = function ($a, $b) {
	if ($a == $b) {
		return;
	}
	var $tmp = this[$a];
	this[$a] = this[$b];
	this[$b] = $tmp;
};



String.prototype.splitAndTrim = function ($delimiter, $limit) {
	var $ss = this.split($delimiter, $limit);
	for (var $i = 0; $i < $ss.length; $i++) {
		$ss[$i] = $ss[$i].trim();
	}
	return $ss;
};
String.prototype.lTrim = function () {
	var $whitespace = new String(" \t\n\r");
	if ($whitespace.indexOf(this.charAt(0)) != -1) {
		var $i = this.length;
		var $j = 0;
		while ($j < $i && $whitespace.indexOf(this.charAt($j)) != -1) {
			$j++;
		}
		return this.substring($j, $i);
	}
	return new String(this);
};
String.prototype.rTrim = function () {
	var $whitespace = new String(" \t\n\r");
	if ($whitespace.indexOf(this.charAt(this.length - 1)) != -1) {
		var $i = this.length - 1;
		while ($i >= 0 && $whitespace.indexOf(this.charAt($i)) != -1) {
			$i--;
		}
		return this.substring(0, $i + 1);
	}
	return new String(this);
};
String.prototype.trim = function () {
	return this.lTrim().rTrim();
};


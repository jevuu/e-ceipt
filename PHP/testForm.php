<html>
<head>
	<title>
	e-Ceipt Data Entry Test Form
	</title>
</head>

<body>
	<form action="testResult.php" method="post">
		Customer name: <input type="text" name="userName" placeholder="Enter a name"><br />
		Items purchased: (For every item name, a price MUST be present)<br />
		<ol>
			<li><input type="text" name="items[itemName][]" placeholder="Item name">...<input type="text" name="items[itemPrice][]" placeholder="Price"></li>
			<li><input type="text" name="items[itemName][]" placeholder="Item name">...<input type="text" name="items[itemPrice][]" placeholder="Price"></li>
			<li><input type="text" name="items[itemName][]" placeholder="Item name">...<input type="text" name="items[itemPrice][]" placeholder="Price"></li>
			<li><input type="text" name="items[itemName][]" placeholder="Item name">...<input type="text" name="items[itemPrice][]" placeholder="Price"></li>
			<li><input type="text" name="items[itemName][]" placeholder="Item name">...<input type="text" name="items[itemPrice][]" placeholder="Price"></li>
		</ol>
		<input type="submit">
	</form>
</body>
</html>
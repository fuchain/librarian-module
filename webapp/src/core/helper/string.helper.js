function shortenString(str, length = 100, ending = "...") {
  return str.length > length
    ? str.substring(0, length - ending.length) + ending
    : str;
};

export default shortenString;

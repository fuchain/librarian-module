function shortenString(str, length = 100, ending = "...") {
    const trimmedString = str.length > length
        ? str.substring(0, length)
        : str;
    return trimmedString.substr(0, Math.min(trimmedString.length, trimmedString.lastIndexOf(" "))) + ending
};

export default shortenString;

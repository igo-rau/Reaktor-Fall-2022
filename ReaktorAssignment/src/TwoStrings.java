public class TwoStrings {
    private String recordKey;
    private String recordValue;

    public TwoStrings(String inputLine) {
        int position = inputLine.indexOf(" = ");
        this.recordKey = inputLine.substring(0, position);
        this.recordValue = inputLine.substring(position + 3, inputLine.length());
        if (this.recordValue.charAt(0) == '\"' && this.recordValue.charAt(this.recordValue.length() - 1) == '\"') {
            this.recordValue = this.recordValue.substring(1, this.recordValue.length() - 1);
        }

    }

    public TwoStrings(String recordKey, String recordValue) {
        this.recordKey = recordKey;
        this.recordValue = recordValue;
    }

    public String getKey() {
        return this.recordKey;
    }

    public String getValue() {
        return this.recordValue;
    }

    public TwoStrings returnSplitted(String inputLine) {
        // Divides String into two Strings - key and value. Divided around " = "
        // Removes quotemarks from value, if any
        int position = inputLine.indexOf(" = ");
        String keyPart = inputLine.substring(0, position);
        String valuePart = inputLine.substring(position + 3, inputLine.length());
        if (valuePart.charAt(0) == '\"' && valuePart.charAt(valuePart.length() - 1) == '\"') {
            valuePart = valuePart.substring(1, valuePart.length() - 1);
        }
        return new TwoStrings(keyPart, valuePart);
    }

    public String toString() {
        return "Key: " + this.recordKey + "; Value: " + this.recordValue;
    }

}

// Text output

importPackage(java.lang);
importPackage(java.io);


framework.addJavaScriptHelp("print", "output 'msg' to stdout and add a new line");

function print(msg) {
    System.out.println(msg);
}


framework.addJavaScriptHelp("eprint", "output 'msg' to stderr and add a new line");

function eprint(msg) {
    System.err.println(msg);
}


framework.addJavaScriptHelp("write", "write 'text' to a file 'fileName' (relative to the working directory) or to stdout if 'fileName' is skipped");

function write(text, fileName) {
    if (arguments.length < 2) {
        System.out.print(text);
    } else {
        dir = framework.getWorkingDirectory();
        file = new File(dir, fileName);
        fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(text);
        bufferedWriter.close();
    }
}

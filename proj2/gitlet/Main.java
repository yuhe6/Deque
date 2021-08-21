package gitlet;


/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {


    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        Repository r = new Repository();

        String firstArg = args[0];
        if (firstArg == null) {
            System.out.print("Please enter a command.");
            System.exit(0);
        }

        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
               if (args.length == 1) {
                   r.init();
                   return;
               }
               else {
                   System.out.println("Incorrect Operands");
               }
            case "add":
                // TODO: handle the `add [filename]` command
                if (args.length == 1) {
                    r.add(args[1]);
                }
                else {
                    System.out.println("Incorrect Operands");
                }
            case "commit":
                // TODO: handle the `commit [filename]` command
                if (args.length == 2) {
                    r.commit(args[1]);
                }
                else {
                    System.out.println("Incorrect Operands");
                }

            case "checkout":
                // TODO: handle the `find [filename]` command
                if (args.length != 2 && args.length != 3 && args.length != 4) {
                    System.out.println("Incorrect Operands");
                } else if ((args.length == 4 && !args[2].equals("--"))
                        || (args.length == 3 && !args[1].equals("--"))) {
                    System.out.println("Incorrect Operands");
                } else {
                    r.checkout(args);
                }

            case "log":
                // TODO: handle the `log` command
                if (args.length == 1) {
                    r.log();
                }
                else {
                    System.out.println("Incorrect Operands");
                }

            default:
                System.out.println("No command with that name exists.");
            }
        }

}
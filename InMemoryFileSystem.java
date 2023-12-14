import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class InMemoryFileSystem {
    private String currentPath;
    private static final String PATH_PREFIX="\\";
    private static final String PATH_SUFFIX=" $ ";    
    private Map<String, Object> fileSystem;
    private Map<String,Object> currentDirectory;
    public InMemoryFileSystem()
    {
        this.fileSystem=new HashMap<>();
        this.currentPath="";
        this.currentDirectory=this.fileSystem;
    }

    public void createDirectory(String directoryName)
    {
        if(this.currentDirectory.containsKey(directoryName))
        {
            System.out.println("A subdirectory or file "+directoryName+" already exists.");
            return;
        }
        else{
            this.currentDirectory.put(directoryName, new HashMap<String,Object>());
        }
    }

    public void changeDirectory(String directoryName)
    {
        if(this.currentDirectory.containsKey(directoryName))
        {
            this.currentDirectory=(HashMap<String,Object>)this.currentDirectory.get(directoryName);
            this.currentPath+="\\"+directoryName;
        }
        else if(directoryName.equals("/") || directoryName.equals("~"))
        {
            this.currentDirectory=this.fileSystem;
            this.currentPath="";
        }
        else if(directoryName.split("\\\\").length>1)
        {
            String[] parts=directoryName.split("\\\\");
            String tempPath=this.currentPath;
            Map<String,Object> tempDirectory=this.fileSystem;
            for(String part:parts)
            {
                if(tempDirectory.containsKey(part))
                {
                    tempDirectory=(HashMap<String,Object>)tempDirectory.get(part);
                    tempPath+="\\"+part;
                }
                else
                {
                    System.out.println("The system cannot find the path specified.");
                    return;
                }
            }
            this.currentDirectory=tempDirectory;
            this.currentPath=tempPath;
        }
        else{
            System.out.println("The system cannot find the path specified.");
        }

    }

    public void list()
    {
        for(Map.Entry<String,Object> entry:this.currentDirectory.entrySet())
        {
            Object value=entry.getValue();
            if(value instanceof String) System.out.println("file - "+entry.getKey());
            if(value instanceof Map) System.out.println("dir - "+entry.getKey());
        }
    }

    public void concatenate(String fileName)
    {
        if(this.currentDirectory.containsKey(fileName) && this.currentDirectory.get(fileName) instanceof String)
        {
            System.out.println(this.currentDirectory.get(fileName));
        }
    }

    public void touch(String fileName)
    {
        if(this.currentDirectory.containsKey(fileName))
        {
            System.out.println("A subdirectory or file "+fileName+" already exists.");
            return;
        }
        else{
            this.currentDirectory.put(fileName,"");
        }
    }

    public void echo(String[] fileData)
    {
        String result = String.join(" ", fileData);
        String[] parts=result.split(">");
        if(parts.length!=2)
        {
            System.out.println("File name not specified.");
            return;
        } 
        this.currentDirectory.put(parts[1].trim(),parts[0]);
    }

    public void remove(String directoryName)
    {
        if(this.currentDirectory.containsKey(directoryName))
        {
            this.currentDirectory.remove(directoryName);
        }
        else if(directoryName.split("\\\\").length>1)
        {
            String[] parts=directoryName.split("\\\\");
            Map<String,Object> tempDirectory=this.fileSystem;
            int i=0;
            for(i=0;i<parts.length-1;i++)
            {
                if(tempDirectory.containsKey(parts[i]))
                {
                    tempDirectory=(HashMap<String,Object>)tempDirectory.get(parts[i]);
                }
                else{
                    System.out.println("The system cannot find the specified file or directory.");
                    return;
                }
            }
            if(tempDirectory.containsKey(parts[i])) tempDirectory.remove(parts[i]);
        }
        else{
            System.out.println("The system cannot find the specified file or directory.");
        }
    }

    public static void main(String gg[])
    {
        
        InMemoryFileSystem fileSystem=new InMemoryFileSystem();
        Scanner scanner = new Scanner(System.in);
        while(true)
        {
            System.out.print(PATH_PREFIX+fileSystem.currentPath+PATH_SUFFIX);
            String userInput = scanner.nextLine();
            userInput=userInput.trim();
            if (userInput.equalsIgnoreCase("exit")) {
                break;
            }
            String[] parts = userInput.split(" ");
                String command = parts[0];
                String[] arguments = new String[parts.length - 1];
                System.arraycopy(parts, 1, arguments, 0, arguments.length);
                switch(command.toLowerCase()){
                    case "mkdir":
                    fileSystem.createDirectory(arguments[0]);
                    break;
                    case "cd":
                    fileSystem.changeDirectory(arguments[0]);
                    break;
                    case "ls":
                    fileSystem.list();
                    break;
                    case "cat":
                    fileSystem.concatenate(arguments[0]);
                    break;
                    case "touch":
                    fileSystem.touch(arguments[0]);
                    break;
                    case "echo":
                    fileSystem.echo(arguments);
                    break;
                    case "mv":
                    break;
                    case "cp":
                    break;
                    case "rm":
                    fileSystem.remove(arguments[0]);
                    break;
                    default:
                    System.out.println("Invalid command");
                    
                }

        }
        scanner.close();
    }


}
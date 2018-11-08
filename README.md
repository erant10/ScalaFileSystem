### In-Memory virtual file system

A functional implementation in scala of a simple CLI for a virtual file system.


#### usage:

- Supported **Directory** methods
    - `$ mkdir <dirname>` - creates and empty directory in the current working directory.
    - `$ cd <dirname>` - changes the current directory into `<dirname>`.
    - `$ ls` - lists the contents of the current directory.
    - `$ rm <dirname>` -  removes the `<dirname>` from the current directory.
    - `$ pwd` - prints the entire current working directory.
    
- Supported **File** methods
    - `$ touch <filename>` -  create a new file with the name `<filename>` inside the current directory.
    - `$ rm <filename>` -  removes the file `<filename>` from the current directory.
    - `$ echo <content (...)> > <filename>` - writes `<content>` into the file `filename`. if the file does'nt exist - 
    creates it with `content`, if it does - overwrites the content
    - `$ echo <content (...)> >> filename` - appends `content` into the file `filename`.
    - `$ cat <filname>` - prints the content of the file `filename`



***Implemented as part of the Rock the JVM scala beginners course.*** 
import os
import sys

if len(sys.argv)!=2:
    print("Peer not specified")
    sys.exit(1)
print(os.getcwd())

peer = sys.argv[1]

filesDirectory = os.getcwd()+"/datasets/"

if not os.path.exists(filesDirectory):
    os.makedirs(filesDirectory)

def generate_files(size_in_bytes, files_count, file_type):
    for i in range(0, files_count):
        extension = ".txt" if file_type=="text" else ".bin"
        filename = "1KB_"+"0"*(6-len(str(i)))+str(i)+"_"+peer+extension
        file_path = os.path.join(filesDirectory, filename)

        with open(file_path, 'wb') as file:
            content = bytes(os.urandom(size_in_bytes))
            file.write(content)

generate_files(1024, 500000, "text")
generate_files(1024*1024, 5000, "text")
generate_files(1024*1024*1024, 5, "binary")


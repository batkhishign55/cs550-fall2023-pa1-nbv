[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/ancXpPFO)
### CS550 Advanced Operating Systems Programming Assignment 1 Repo
**Illinois Institute of Technology**  

**Team Name**: NBV  
**Students**: 
* Batkhishig Dulamsurankhor (bdulamsurankhor@hawk.iit.edu)   
* Nitin Singh (nsingh33@hawk.iit.edu)  
* Vaishnavi Papudesi Babu (vpapudesibabu@hawk.iit.edu)   

**Building**

Run ant command, it will generate source files in bin folder.
```console
ant
``` 
**Running**

Go to bin folder.
```console
cd bin
``` 
Create folder named files. Add some files to share there.

Start peer client app.
```console
java src.peer.PeerMain
``` 
Start central indexing server app.
```console
java src.cis.CisMain
```

<img width="1136" alt="Screenshot 2023-09-26 at 10 51 43 PM" src="https://github.com/datasys-classrooms/cs550-fall2023-pa1-nbv/assets/145067050/9162f4b6-e2b3-4420-baf6-9a7993cac243">


**Registration:**
<img width="1138" alt="Screenshot 2023-09-26 at 4 44 31 PM" src="https://github.com/datasys-classrooms/cs550-fall2023-pa1-nbv/assets/145067050/daace1d4-3ce4-4fa7-9d3f-87c8254d45e0">


To run files-generator.py, 
Execute `python files-generator.py <peerID>`, eg. `python files-generater.py peer1`

**Weak Scaling Scalability Study**
![image](https://github.com/datasys-classrooms/cs550-fall2023-pa1-nbv/assets/145067050/0a4412e0-9b5a-48b1-b1a6-8db3bd58539d)

**Strong Scaling Scalability Study**
<img width="970" alt="Screenshot 2023-09-28 at 10 50 26 PM" src="https://github.com/datasys-classrooms/cs550-fall2023-pa1-nbv/assets/145067050/29f87772-37f8-4152-8921-3dc1903de605">

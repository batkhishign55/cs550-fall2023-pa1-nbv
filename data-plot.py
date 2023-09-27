import matplotlib.pyplot as plt

# The number of nodes we have (VMs)
nodes = [1, 2]

# Average search times for each requests per peer (time per request)
average_times = [2, 4]

# Standard deviations
std_devs = [1, 2]


# average search times
plt.figure(figsize=(10, 5))
plt.subplot(1, 2, 1)
plt.plot(nodes, average_times, marker='o')
plt.title('Average Search Time vs. Nodes')
plt.xlabel('Number of Nodes')
plt.ylabel('Average Search Time (seconds)')

# standard deviations of search time
plt.subplot(1, 2, 2)
plt.plot(nodes, std_devs, marker='o')
plt.title('Standard Deviation of Search Time vs. Nodes')
plt.xlabel('Nodes')
plt.ylabel('Standard Deviation')

plt.tight_layout()
plt.show()

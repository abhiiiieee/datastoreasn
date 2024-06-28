from rediscluster import RedisCluster

# Connect to Redis Cluster
startup_nodes = [
    {"host": "localhost", "port": "7000"},
    {"host": "localhost", "port": "7001"},
    {"host": "localhost", "port": "7002"}
]
r = RedisCluster(startup_nodes=startup_nodes, decode_responses=True)

# CRUD Operations
def put(key, value):
    r.set(key, value)

def get(key):
    value = r.get(key)
    if value is None:
        return f"Key '{key}' does not exist."
    return value

def delete(key):
    result = r.delete(key)
    if result == 1:
        return f"Key '{key}' has been deleted."
    else:
        return f"Key '{key}' does not exist."

# Test CRUD operations
if __name__ == "__main__":
    key = "Abhishek"
    value = "Practo"

    # Put operation
    put(key, value)
    print(f"Put: Key={key}, Value={value}")

    # Get operation
    result = get(key)
    print(f"Get: Key={key}, Value={result}")

    # Delete operation
    delete_result = delete(key)
    print(f"Delete: {delete_result}")

    # Attempt to get the key after deletion
    result = get(key)
    print(f"Get: Key={key}, Value={result}")
# OCARIoT SPLat Configuration

### Prerequisites

1. Docker Engine 18.06.0+
   - Follow all the steps present in the [official documentation](https://docs.docker.com/install/linux/docker-ce/ubuntu/#install-docker-ce);
2. Docker Compose 1.22.0+
   - Follow all the steps present in the [official documentation](https://docs.docker.com/compose/install/);
3. Ensure that the command `yq` is available in` bash`. For more information [click here](https://github.com/mikefarah/yq) to consult the documentation;
4. Ensure that `docker-composer` is mapped to` System.getenv("PATH") `.

### Execution

1. Clone the project to your workspace with `git clone https://github.com/lucas-barbosa-oliveira/splat-ticket.git`;
2. To import the project, for this, go to `File >> Import ...` then in `General` click on` Existing Projects into Workspace`. Then, go to `Select root directory` and select the project previously placed in the workspace. In `Projects` select the project and click on` Finish`;
3. Go to `src`, open the` splat.SPLatOcariot.java` class;
4. Add as the first execution variable the path to the [system-test](https://github.com/ocariot/system-test/tree/master) project located on your machine;
5. Run the project.



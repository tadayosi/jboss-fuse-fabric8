/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package io.fabric8.commands;

import io.fabric8.api.FabricService;
import io.fabric8.api.RuntimeProperties;
import io.fabric8.api.ZooKeeperClusterService;
import io.fabric8.api.scr.ValidatingReference;
import io.fabric8.boot.commands.support.AbstractCommandComponent;
import io.fabric8.boot.commands.support.ContainerCompleter;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.basic.AbstractCommand;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.service.command.Function;

@Component(immediate = true)
@Service({ Function.class, AbstractCommand.class })
@org.apache.felix.scr.annotations.Properties({
        @Property(name = "osgi.command.scope", value = ContainerDelete.SCOPE_VALUE),
        @Property(name = "osgi.command.function", value = ContainerDelete.FUNCTION_VALUE)
})
public class ContainerDelete extends AbstractCommandComponent {

    public static final String SCOPE_VALUE = "fabric";
    public static final String FUNCTION_VALUE = "container-delete";
    public static final String DESCRIPTION = "Stops and deletes an existing container";

    @Reference(referenceInterface = FabricService.class)
    private final ValidatingReference<FabricService> fabricService = new ValidatingReference<FabricService>();
    @Reference(referenceInterface = ZooKeeperClusterService.class)
    private final ValidatingReference<ZooKeeperClusterService> clusterService = new ValidatingReference<ZooKeeperClusterService>();
    @Reference(referenceInterface = RuntimeProperties.class)
    private final ValidatingReference<RuntimeProperties> runtimeProperties = new ValidatingReference<RuntimeProperties>();

    // Completers
    @Reference(referenceInterface = ContainerCompleter.class, bind = "bindContainerCompleter", unbind = "unbindContainerCompleter")
    private ContainerCompleter containerCompleter; // dummy field

    @Activate
    void activate() {
        activateComponent();
    }

    @Deactivate
    void deactivate() {
        deactivateComponent();
    }

    @Override
    public Action createNewAction() {
        assertValid();
        return new ContainerDeleteAction(fabricService.get(), clusterService.get(), runtimeProperties.get());
    }

    void bindFabricService(FabricService fabricService) {
        this.fabricService.bind(fabricService);
    }

    void unbindFabricService(FabricService fabricService) {
        this.fabricService.unbind(fabricService);
    }

    void bindClusterService(ZooKeeperClusterService clusterService) {
        this.clusterService.bind(clusterService);
    }

    void unbindClusterService(ZooKeeperClusterService clusterService) {
        this.clusterService.unbind(clusterService);
    }

    void bindContainerCompleter(ContainerCompleter completer) {
        bindCompleter(completer);
    }

    void unbindContainerCompleter(ContainerCompleter completer) {
        unbindCompleter(completer);
    }

    void bindRuntimeProperties(RuntimeProperties service) {
        this.runtimeProperties.bind(service);
    }

    void unbindRuntimeProperties(RuntimeProperties service) {
        this.runtimeProperties.unbind(service);
    }

}

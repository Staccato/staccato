package com.readytalk.staccato.database.migration.workflow;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

import com.readytalk.staccato.database.migration.MigrationException;
import com.readytalk.staccato.database.migration.MigrationResult;
import com.readytalk.staccato.database.migration.MigrationRuntime;
import com.readytalk.staccato.database.migration.annotation.MigrationAnnotationParser;
import com.readytalk.staccato.database.migration.annotation.WorkflowStep;
import com.readytalk.staccato.database.migration.script.DynamicLanguageScript;
import com.google.inject.Inject;

/**
 * Default implemenation for executing migrations
 *
 * @author jhumphrey
 */
public class MigrationWorkflowServiceImpl<T extends Annotation> implements MigrationWorkflowService {

  public static final Logger logger = Logger.getLogger(MigrationWorkflowServiceImpl.class);

  private MigrationAnnotationParser annotationParser;

  @Inject
  public MigrationWorkflowServiceImpl(MigrationAnnotationParser annotationParser) {
    this.annotationParser = annotationParser;
  }

  @Override
  @SuppressWarnings("unchecked")
  public MigrationResult executeWorkflow(DynamicLanguageScript script, Class<? extends Annotation>[] migrationWorkflow, MigrationRuntime migrationRuntime) {

    String beginOutput = "Executing migration workflow: ";
    for (Class<? extends Annotation> aClass : migrationWorkflow) {
      beginOutput += aClass.getSimpleName() + " ";
    }

    logger.debug(beginOutput);

    MigrationResult migrationResult = new MigrationResult();

    for (Class<? extends Annotation> workflowStep : migrationWorkflow) {

      Annotation annotation = annotationParser.getMethodAnnotation(script.getScriptInstance(), workflowStep);

      if (annotation != null) {
        Class<? extends WorkflowStepExecutor<T>> executor = (Class<? extends WorkflowStepExecutor<T>>) workflowStep.getAnnotation(WorkflowStep.class).executedBy();

        if (executor != null) {
          // instantiate and execute the step
          try {
            WorkflowStepExecutor<T> stepExecutorInstance = executor.newInstance();
            stepExecutorInstance.initialize((T) annotation);
            Object executionResult = stepExecutorInstance.execute(script.getScriptInstance(), new WorkflowContext(annotationParser, migrationRuntime));
            migrationResult.getResultMap().put(annotation.annotationType(), executionResult);
          } catch (InstantiationException e) {
            throw new MigrationException("Unable to instantiate workflow step: " + workflowStep.getSimpleName(), e);
          } catch (IllegalAccessException e) {
            throw new MigrationException("Unable to access workflow step class: " + workflowStep.getSimpleName(), e);
          } catch (InvocationTargetException e) {
            throw new MigrationException("Error invoking workflow step: " + workflowStep.getSimpleName(), e);
          }
        }
      } else {
        logger.debug("Skipping workflow step @" + workflowStep.getSimpleName() + ". No annotated methods found in script: " + script.getFilename());
      }
    }

    return migrationResult;
  }
}
